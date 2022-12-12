// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.projections;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.*;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;

import java.util.*;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class ProjectionSource {

  public static final String CURRENT_DATA = "currentData";
  public static final String PREVIOUS_DATA = "previousData";
  public static final String TYPED_EVENT = "typedEvent";
  public static final String COLLECTION_TYPED_EVENTS = "%s.FromAll(typedEvent.%s)";
  public final String name;
  public final String dataObjectName;
  public final String mergeParameters;
  public final String sourceFieldsCarrierName;
  public final List<String> collectionMutations = new ArrayList<>();
  public final List<String> dataObjectInitializers = new ArrayList<>();

  public static List<ProjectionSource> from(final ProjectionType projectionType, final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> events,
                                            final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
        .filter(method -> method.hasAny(Label.DOMAIN_EVENT))
        .map(method -> method.retrieveOneRelated(Label.DOMAIN_EVENT))
        .map(event -> new ProjectionSource(projectionType, event, events, valueObjects))
        .distinct().collect(Collectors.toList());
  }

  private ProjectionSource(final ProjectionType projectionType, final CodeGenerationParameter event,
                           final List<CodeGenerationParameter> events, final List<CodeGenerationParameter> valueObjects) {
    this.name = event.value;
    this.sourceFieldsCarrierName = resolveSourceFieldsCarrierName(event);
    this.mergeParameters = resolveMergeParameters(projectionType, event, events);
    this.dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(event.parent(Label.AGGREGATE).value);
    this.dataObjectInitializers.addAll(resolveDataObjectInitializers(projectionType, event, events, valueObjects));
    this.collectionMutations.addAll(resolveCollectionMutations(projectionType, event, events));
  }

  private String resolveMergeParameters(final ProjectionType projectionType, final CodeGenerationParameter event,
                                        final List<CodeGenerationParameter> events) {
    return projectionType.isEventBased() ? resolveEventBasedMergeParameters(event, events) :
        resolveOperationBasedMergeParameters(event, events);
  }

  private List<String> resolveCollectionMutations(final ProjectionType projectionType, final CodeGenerationParameter event,
                                                  final List<CodeGenerationParameter> events) {
    if (projectionType.isOperationBased()) {
      return Collections.emptyList();
    }
    return DomainEventDetail.eventWithName(event.value, events).retrieveAllRelated(Label.STATE_FIELD)
        .map(eventField -> Tuple2.from(eventField, eventField.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName)))
        .filter(tuple -> tuple._2.isSingleParameterBased())
        .flatMap(tuple -> {
          if (FieldDetail.isAssignableToValueObject(tuple._1)) {
            return tuple._2.resolveStatements(Dialect.C_SHARP, sourceFieldsCarrierName, tuple._1).stream();
          }
          return tuple._2.resolveStatements(Dialect.C_SHARP, sourceFieldsCarrierName, TYPED_EVENT, tuple._1).stream();
        })
        .collect(Collectors.toList());
  }

  private String resolveEventBasedMergeParameters(final CodeGenerationParameter event,
                                                  final List<CodeGenerationParameter> events) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).map(field -> {
      final Optional<CodeGenerationParameter> eventField =
          DomainEventDetail.fieldWithName(event.value, field.value, events);

      if (eventField.isPresent()) {
        final CollectionMutation collectionMutation =
            eventField.get().retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

        if (collectionMutation.isSingleParameterBased()) {
          return sourceFieldsCarrierName + "." + toPascalCase(field.value);
        } else if (FieldDetail.isValueObjectCollection(field)) {
          final String valueObjectType = field.retrieveRelatedValue(Label.FIELD_TYPE);
          final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(valueObjectType);
          return String.format(COLLECTION_TYPED_EVENTS, dataObjectName, toPascalCase(field.value));
        } else if (ValueObjectDetail.isValueObject(field)) {
          return field.value;
        } else {
          return TYPED_EVENT + "." + toPascalCase(field.value);
        }
      }
      if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        if(FieldDetail.isValueObjectCollection(field))
          return FieldDetail.resolveDefaultValue(aggregate, field.value).replace(">", "Data>");
        return FieldDetail.resolveDefaultValue(aggregate, field.value);
      }
      return sourceFieldsCarrierName + "." + toPascalCase(field.value);
    }).collect(Collectors.joining(", "));
  }

  private String resolveOperationBasedMergeParameters(final CodeGenerationParameter event,
                                                      final List<CodeGenerationParameter> events) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).map(field -> {
      if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        return sourceFieldsCarrierName + "." + toPascalCase(field.value);
      }
      if (DomainEventDetail.hasField(event.value, field.value, events)) {
        return String.format("%s.%s", CURRENT_DATA, toPascalCase(field.value));
      }
      return String.format("%s.%s", PREVIOUS_DATA, toPascalCase(field.value));
    }).collect(Collectors.joining(", "));
  }

  private List<String> resolveDataObjectInitializers(final ProjectionType projectionType,
                                                     final CodeGenerationParameter event,
                                                     final List<CodeGenerationParameter> events,
                                                     final List<CodeGenerationParameter> valueObjects) {
    if (projectionType.isOperationBased()) {
      return Collections.emptyList();
    }
    return Formatters.Variables.format(Formatters.Variables.Style.EVENT_BASED_DATA_OBJECT_INITIALIZER,
        Dialect.C_SHARP, DomainEventDetail.eventWithName(event.value, events), valueObjects.stream());
  }

  private String resolveSourceFieldsCarrierName(final CodeGenerationParameter event) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate) || !AggregateDetail.hasFactoryMethod(aggregate)) {
      return CURRENT_DATA;
    }
    return PREVIOUS_DATA;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjectionSource that = (ProjectionSource) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}
