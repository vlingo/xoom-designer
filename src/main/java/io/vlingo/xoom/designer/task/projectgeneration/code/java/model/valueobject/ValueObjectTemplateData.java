// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail.resolvePackage;

public class ValueObjectTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage,
                                        final Dialect dialect,
                                        final Stream<CodeGenerationParameter> valueObjects) {
    final Function<CodeGenerationParameter, TemplateData> mapper =
            vo -> new ValueObjectTemplateData(basePackage, dialect, vo);

    return valueObjects.map(mapper).collect(Collectors.toList());
  }

  private ValueObjectTemplateData(final String basePackage,
                                  final Dialect dialect,
                                  final CodeGenerationParameter valueObject) {
    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, resolvePackage(basePackage))
                    .and(VALUE_OBJECT_NAME, valueObject.value)
                    .and(CONSTRUCTOR_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(valueObject))
                    .and(CONSTRUCTOR_INVOCATION_PARAMETERS, Formatters.Arguments.VALUE_OBJECT_CONSTRUCTOR_INVOCATION.format(valueObject))
                    .and(MEMBERS, Formatters.Fields.format(Formatters.Fields.Style.MEMBER_DECLARATION, dialect, valueObject))
                    .and(MEMBER_NAMES, valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).map(p -> p.value).collect(Collectors.toList()))
                    .and(MEMBERS_ASSIGNMENT, Formatters.Fields.format(Formatters.Fields.Style.ASSIGNMENT, dialect, valueObject))
                    .addImports(ValueObjectDetail.resolveFieldsImports(valueObject));
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.VALUE_OBJECT;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(parameters.find(VALUE_OBJECT_NAME), parameters);
  }
}
