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
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.exchange.ExchangeRole;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationSetup.DEFAULT_SCHEMA_VERSION;
import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationSetup.EVENT_SCHEMA_CATEGORY;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;
import static java.util.stream.Collectors.toList;

public class DomainEventSpecificationTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final List<CodeGenerationParameter> exchanges) {
    final Predicate<CodeGenerationParameter> onlyProducers =
            exchange -> exchange.retrieveRelatedValue(Label.ROLE, ExchangeRole::of).isProducer();

    final CodeGenerationParameter producerExchange =
            exchanges.stream().filter(onlyProducers).findAny().get();

    final String schemaGroup = producerExchange.retrieveRelatedValue(Label.SCHEMA_GROUP);

    return exchanges.stream().filter(exchange -> exchange.hasAny(Label.DOMAIN_EVENT))
            .flatMap(exchange -> exchange.retrieveAllRelated(Label.DOMAIN_EVENT))
            .map(e -> new DomainEventSpecificationTemplateData(EVENT_SCHEMA_CATEGORY, schemaGroup, e))
            .collect(toList());
  }

  private DomainEventSpecificationTemplateData(final String schemaCategory,
                                               final String schemaGroup,
                                               final CodeGenerationParameter publishedDialect) {
    this.parameters =
            TemplateParameters.with(SCHEMA_CATEGORY, schemaCategory)
                    .and(SCHEMATA_SPECIFICATION_NAME, publishedDialect.value)
                    .and(FIELD_DECLARATIONS, generateFieldDeclarations(schemaGroup, publishedDialect))
                    .and(SCHEMATA_FILE, true);
  }

  private List<String> generateFieldDeclarations(final String schemaGroup, final CodeGenerationParameter publishedDialect) {
    final CodeGenerationParameter aggregate = publishedDialect.parent(Label.AGGREGATE);
    final CodeGenerationParameter event = AggregateDetail.eventWithName(aggregate, publishedDialect.value);
    final Stream<CodeGenerationParameter> stateFields = event.retrieveAllRelated(Label.STATE_FIELD);
    return stateFields.map(field -> AggregateDetail.stateFieldWithName(aggregate, field.value))
            .map(field -> resolveFieldType(schemaGroup, field)).collect(toList());
  }

  private String resolveFieldType(final String schemaGroup,
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
