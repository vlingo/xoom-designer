// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.resource;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;
import static java.util.stream.Collectors.toList;

public class RestResourceTemplateDataFactory {

  public static List<TemplateData> build(final CodeGenerationParameters parameters,
                                         final List<Content> contents) {
    final String basePackage = parameters.retrieveValue(PACKAGE);
    final Boolean useCQRS = parameters.retrieveValue(CQRS, Boolean::valueOf);
    final Language language = parameters.retrieveValue(Label.LANGUAGE, Language::valueOf);
    final List<CodeGenerationParameter> valueObjects = parameters.retrieveAll(VALUE_OBJECT).collect(toList());

    final Function<CodeGenerationParameter, TemplateData> mapper =
            aggregateParameter -> new RestResourceTemplateData(basePackage, language, aggregateParameter, valueObjects, contents, useCQRS);

    return parameters.retrieveAll(AGGREGATE)
            .filter(aggregateParameter -> aggregateParameter.hasAny(ROUTE_SIGNATURE))
            .map(mapper).collect(Collectors.toList());
  }

}