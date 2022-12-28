// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.autodispatch;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AutoDispatchMappingTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationParameters parameters,
                                         final List<Content> contents) {
    final String basePackage =
            parameters.retrieveValue(Label.PACKAGE);

    final boolean useCQRS =
            parameters.retrieveValue(Label.CQRS, Boolean::valueOf);

    final Dialect dialect =
            parameters.retrieveValue(Label.DIALECT, Dialect::valueOf);

    final List<CodeGenerationParameter> valueObjects =
            parameters.retrieveAll(Label.VALUE_OBJECT).collect(toList());

    final Function<CodeGenerationParameter, Stream<TemplateData>> mapper = aggregate ->
            Stream.of(new AutoDispatchMappingTemplateData(basePackage,
                            aggregate, useCQRS, contents),
                    new AutoDispatchHandlersMappingTemplateData(basePackage, dialect,
                            aggregate, valueObjects, contents, useCQRS));

    return parameters.retrieveAll(Label.AGGREGATE).flatMap(mapper).collect(toList());
  }
}
