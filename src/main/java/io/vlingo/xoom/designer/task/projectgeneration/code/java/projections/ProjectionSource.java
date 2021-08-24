// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.projections;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.domainevent.DomainEventDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.*;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.DATA_OBJECT;

public class ProjectionSource {

  public final String name;
  public final String dataObjectName;
  public final String mergeParameters;
  public final String sourceFieldsCarrierName;
  public final List<String> collectionMutations = new ArrayList<>();
  public final List<String> dataObjectInitializers = new ArrayList<>();

  public static List<ProjectionSource> from(final ProjectionType projectionType,
                                            final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> events,
                                            final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD)
            .filter(method -> method.hasAny(DOMAIN_EVENT))
            .map(method -> method.retrieveOneRelated(DOMAIN_EVENT))
            .map(event -> new ProjectionSource(projectionType, event, events, valueObjects))
            .distinct().collect(Collectors.toList());
  }

  private ProjectionSource(final ProjectionType projectionType,
                           final CodeGenerationParameter event,
                           final List<CodeGenerationParameter> events,
                           final List<CodeGenerationParameter> valueObjects) {
    this.name = event.value;
    this.sourceFieldsCarrierName = resolveSourceFieldsCarrierName(event);
    this.mergeParameters = resolveMergeParameters(projectionType, event, events);
    this.dataObjectName = DATA_OBJECT.resolveClassname(event.parent(Label.AGGREGATE).value);
    this.dataObjectInitializers.addAll(resolveDataObjectInitializers(projectionType, event, events, valueObjects));
    this.collectionMutations.addAll(resolveCollectionMutations(projectionType, event, events));
  }

  private String resolveMergeParameters(final ProjectionType projectionType,
                                        final CodeGenerationParameter event,
                                        final List<CodeGenerationParameter> events) {
    return projectionType.isEventBased() ?
            resolveEventBasedMergeParameters(event, events) :
            resolveOperationBasedMergeParameters(event, events);
  }

  private List<String> resolveCollectionMutations(final ProjectionType projectionType,
                                                  final CodeGenerationParameter event,
                                                  final List<CodeGenerationParameter> events) {
    if(projectionType.isOperationBased()) {
      return Collections.emptyList();
    }
    return DomainEventDetail.eventWithName(event.value, events).retrieveAllRelated(STATE_FIELD)
            .map(eventField -> Tuple2.from(eventField, eventField.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName)))
            .filter(tuple -> tuple._2.isSingleParameterBased())
            .flatMap(tuple -> tuple._2.resolveStatements(sourceFieldsCarrierName, tuple._1).stream())
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
                eventField.get().retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

        if (collectionMutation.isSingleParameterBased()) {
          return sourceFieldsCarrierName + "." + field.value;
        } else if (FieldDetail.isValueObjectCollection(field)) {
          final String valueObjectType = field.retrieveRelatedValue(FIELD_TYPE);
          final String dataObjectName = DATA_OBJECT.resolveClassname(valueObjectType);
          return String.format("%s.fromAll(typedEvent.%s)", dataObjectName, field.value);
        } else if (ValueObjectDetail.isValueObject(field)) {
          return field.value;
        } else {
          return "typedEvent." + field.value;
        }
      }
      if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        return FieldDetail.resolveDefaultValue(aggregate, field.value);
      }
      return sourceFieldsCarrierName + "." + field.value;
    }).collect(Collectors.joining(", "));
  }

  private String resolveOperationBasedMergeParameters(final CodeGenerationParameter event,
                                                      final List<CodeGenerationParameter> events) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).map(field -> {
      if (DomainEventDetail.hasField(event.value, field.value, events) ||
              DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        return sourceFieldsCarrierName + "." + field.value;
      }
      return "previousData." + field.value;
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
            Dialect.findDefault(), DomainEventDetail.eventWithName(event.value, events), valueObjects.stream());
  }

  private String resolveSourceFieldsCarrierName(final CodeGenerationParameter event) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
      return "currentData";
    }
    return "previousData";
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
