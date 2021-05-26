// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties.DATA_SCHEMA_CATEGORY;
import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties.DEFAULT_SCHEMA_VERSION;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class ValueObjectSpecificationTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<CodeGenerationParameter> exchanges,
                                        final List<CodeGenerationParameter> valueObjects) {
    final Predicate<CodeGenerationParameter> onlyProducers =
            exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer();

    final CodeGenerationParameter producerExchange =
            exchanges.stream().filter(onlyProducers).findAny().get();

    final String schemaGroup = producerExchange.retrieveRelatedValue(Label.SCHEMA_GROUP);

    final Stream<CodeGenerationParameter> publishedValueObjects =
            ValueObjectDetail.findPublishedValueObjects(exchanges, valueObjects);

    return ValueObjectDetail.orderByDependency(publishedValueObjects).map(vo -> {
      return new ValueObjectSpecificationTemplateData(DATA_SCHEMA_CATEGORY, schemaGroup, vo);
    }).collect(toList());
  }

  private ValueObjectSpecificationTemplateData(final String schemaCategory,
                                               final String schemaGroup,
                                               final CodeGenerationParameter publishedDialect) {
    this.parameters =
            TemplateParameters.with(SCHEMA_CATEGORY, schemaCategory)
                    .and(SCHEMATA_SPECIFICATION_NAME, publishedDialect.value)
                    .and(FIELD_DECLARATIONS, generateFieldDeclarations(schemaGroup, publishedDialect))
                    .and(SCHEMATA_FILE, true);
  }

  private List<String> generateFieldDeclarations(final String schemaGroup, final CodeGenerationParameter publishedDialect) {
    final Stream<CodeGenerationParameter> valueObjectFields =
            publishedDialect.retrieveAllRelated(Label.VALUE_OBJECT_FIELD);

    return valueObjectFields.map(field -> resolveFieldDeclaraction(schemaGroup, field)).collect(toList());
  }

  private String resolveFieldDeclaraction(final String schemaGroup,
                                          final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    if (ValueObjectDetail.isValueObject(field)) {
      return String.format("%s:%s:%s", schemaGroup, fieldType, DEFAULT_SCHEMA_VERSION) + " " + field.value;
    }
    return fieldType.toLowerCase() + " " + field.value;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.SCHEMATA_SPECIFICATION;
  }
}
