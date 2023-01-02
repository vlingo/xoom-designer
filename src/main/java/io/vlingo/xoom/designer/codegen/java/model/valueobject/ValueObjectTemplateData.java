// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model.valueobject;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            TemplateParameters.with(TemplateParameter.PACKAGE_NAME, ValueObjectDetail.resolvePackage(basePackage))
                    .and(TemplateParameter.VALUE_OBJECT_NAME, valueObject.value)
                    .and(TemplateParameter.CONSTRUCTOR_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(valueObject))
                    .and(TemplateParameter.CONSTRUCTOR_INVOCATION_PARAMETERS, Formatters.Arguments.VALUE_OBJECT_CONSTRUCTOR_INVOCATION.format(valueObject))
                    .and(TemplateParameter.MEMBERS, Formatters.Fields.format(Formatters.Fields.Style.MEMBER_DECLARATION, dialect, valueObject))
                    .and(TemplateParameter.MEMBER_NAMES, valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).map(p -> p.value).collect(Collectors.toList()))
                    .and(TemplateParameter.MEMBERS_ASSIGNMENT, Formatters.Fields.format(Formatters.Fields.Style.ASSIGNMENT, dialect, valueObject))
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
    return standard().resolveFilename(parameters.find(TemplateParameter.VALUE_OBJECT_NAME), parameters);
  }
}
