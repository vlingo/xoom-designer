// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.dataobject;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.language.Language;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Fields.Style;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.formatting.Formatters.Variables.Style.DATA_TO_VALUE_OBJECT_TRANSLATION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.VALUE_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.VALUE_OBJECT_FIELD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;

public class ValueDataObjectTemplateData extends TemplateData {

  private final String valueObjectName;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String basePackage,
                                        final Language language,
                                        final List<CodeGenerationParameter> valueObjects,
                                        final List<Content> contents) {
    final String packageName = String.format("%s.%s", basePackage, "infrastructure");

    final Function<CodeGenerationParameter, TemplateData> mapper =
            valueObject -> new ValueDataObjectTemplateData(packageName, language, valueObject, valueObjects, contents);

    return valueObjects.stream().map(mapper).collect(Collectors.toList());
  }

  private ValueDataObjectTemplateData(final String packageName,
                                      final Language language,
                                      final CodeGenerationParameter valueObject,
                                      final List<CodeGenerationParameter> valueObjects,
                                      final List<Content> contents) {
    this.valueObjectName = valueObject.value;

    final List<String> valueObjectTranslations =
            Formatters.Variables.format(DATA_TO_VALUE_OBJECT_TRANSLATION, language, valueObject, valueObjects.stream());

    this.parameters =
            TemplateParameters.with(PACKAGE_NAME, packageName).and(VALUE_OBJECT_NAME, valueObjectName)
                    .and(STATIC_FACTORY_METHODS, StaticFactoryMethod.from(valueObject))
                    .and(DATA_VALUE_OBJECT_NAME, standard().resolveClassname(valueObjectName))
                    .and(VALUE_OBJECT_FIELDS, joinValueObjectFields(valueObject))
                    .and(CONSTRUCTOR_PARAMETERS, Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR.format(valueObject))
                    .and(MEMBERS, Formatters.Fields.format(Style.DATA_OBJECT_MEMBER_DECLARATION, language, valueObject))
                    .and(MEMBER_NAMES, valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD).map(p -> p.value).collect(Collectors.toList()))
                    .and(MEMBERS_ASSIGNMENT, Formatters.Fields.format(Style.DATA_VALUE_OBJECT_ASSIGNMENT, language, valueObject))
                    .addImport(CodeElementFormatter.importAllFrom(ContentQuery.findPackage(VALUE_OBJECT, contents)))
                    .addImports(ValueObjectDetail.resolveFieldsImports(valueObject))
                    .and(VALUE_OBJECT_TRANSLATIONS, valueObjectTranslations);
  }

  private String joinValueObjectFields(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .map(field -> {
              if (FieldDetail.isValueObjectCollection(field)) {
                return ValueObjectDetail.translateDataObjectCollection(field.value, field);
              } else {
                return field.value;
              }
            }).collect(Collectors.joining(", "));
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return DesignerTemplateStandard.DATA_OBJECT;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(valueObjectName, parameters);
  }
}
