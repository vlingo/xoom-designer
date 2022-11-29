// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.dataobject;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.StaticFactoryMethod;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValueDataObjectTemplateData extends TemplateData {

  private final String valueObjectName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage, final Dialect dialect,
                                        final List<CodeGenerationParameter> valueObjects, final List<Content> contents) {
    final String packageName = String.format("%s.%s", basePackage, "Infrastructure");

    final Function<CodeGenerationParameter, TemplateData> mapper = valueObject ->
        new ValueDataObjectTemplateData(packageName, dialect, valueObject, valueObjects, contents);

    return valueObjects.stream().map(mapper).collect(Collectors.toList());
  }

  private ValueDataObjectTemplateData(final String packageName, final Dialect dialect, final CodeGenerationParameter valueObject,
                                      final List<CodeGenerationParameter> valueObjects, final List<Content> contents) {
    final String emptyObjectArguments = ValueObjectDetail.resolveEmptyObjectArguments(valueObject);
    final List<String> valueObjectTranslations = Formatters.Variables
        .format(Formatters.Variables.Style.DATA_TO_VALUE_OBJECT_TRANSLATION, dialect, valueObject, valueObjects.stream());

    this.valueObjectName = valueObject.value;
    this.parameters = TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.VALUE_OBJECT_NAME, valueObjectName)
        .and(TemplateParameter.STATIC_FACTORY_METHODS, StaticFactoryMethod.from(valueObject))
        .and(TemplateParameter.DATA_VALUE_OBJECT_NAME, standard().resolveClassname(valueObjectName))
        .and(TemplateParameter.VALUE_OBJECT_FIELDS, ValueObjectDetail.joinValueObjectFields(valueObject))
        .and(TemplateParameter.CONSTRUCTOR_PARAMETERS, Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR.format(valueObject))
        .and(TemplateParameter.MEMBERS, Formatters.Fields.format(Formatters.Fields.Style.DATA_OBJECT_MEMBER_DECLARATION, dialect, valueObject))
        .and(TemplateParameter.MEMBER_NAMES, ValueObjectDetail.resolveFieldsNames(valueObject))
        .and(TemplateParameter.MEMBERS_ASSIGNMENT, Formatters.Fields.format(Formatters.Fields.Style.DATA_VALUE_OBJECT_ASSIGNMENT, dialect, valueObject))
        .addImport(ContentQuery.findPackage(CsharpTemplateStandard.VALUE_OBJECT, contents))
        .addImports(ValueObjectDetail.resolveFieldsImports(valueObject))
        .and(TemplateParameter.VALUE_OBJECT_TRANSLATIONS, valueObjectTranslations)
        .and(TemplateParameter.EMPTY_OBJECT_ARGUMENTS, emptyObjectArguments);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.DATA_OBJECT;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(valueObjectName, parameters);
  }
}
