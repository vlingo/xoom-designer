// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.projections;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.Formatters;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.domainevent.DomainEventDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionSource {

  private final String name;
  private final String dataObjectName;
  private final String mergeParameters;
  private List<String> dataObjectInitializers = new ArrayList<>();

  public static List<ProjectionSource> from(final ProjectionType projectionType,
                                            final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> events,
                                            final List<CodeGenerationParameter> valueObjects) {
    return aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).map(method -> method.retrieveOneRelated(Label.DOMAIN_EVENT))
            .map(event -> new ProjectionSource(projectionType, event, events, valueObjects))
            .collect(Collectors.toList());
  }

  private ProjectionSource(final ProjectionType projectionType,
                           final CodeGenerationParameter event,
                           final List<CodeGenerationParameter> events,
                           final List<CodeGenerationParameter> valueObjects) {
    this.name = event.value;
    this.mergeParameters = resolveMergeParameters(projectionType, event, events);
    this.dataObjectName = JavaTemplateStandard.DATA_OBJECT.resolveClassname(event.parent(Label.AGGREGATE).value);
    this.dataObjectInitializers.addAll(resolveDataObjectInitializers(projectionType, event, events, valueObjects));
  }

  private String resolveMergeParameters(final ProjectionType projectionType,
                                        final CodeGenerationParameter event,
                                        final List<CodeGenerationParameter> events) {
    return projectionType.isEventBased() ?
            resolveEventBasedMergeParameters(event, events) :
            resolveOperationBasedMergeParameters(event, events);
  }

  private String resolveEventBasedMergeParameters(final CodeGenerationParameter event,
                                                  final List<CodeGenerationParameter> events) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).map(field -> {
      if (DomainEventDetail.hasField(event.value, field.value, events)) {
        if (ValueObjectDetail.isValueObject(field)) {
          return field.value;
        }
        return "typedEvent." + field.value;
      }
      if (DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        return FieldDetail.resolveDefaultValue(aggregate, field.value);
      }
      return "previousData." + field.value;
    }).collect(Collectors.joining(", "));
  }

  private String resolveOperationBasedMergeParameters(final CodeGenerationParameter event,
                                                      final List<CodeGenerationParameter> events) {
    final CodeGenerationParameter aggregate = event.parent(Label.AGGREGATE);
    return aggregate.retrieveAllRelated(Label.STATE_FIELD).map(field -> {
      if (DomainEventDetail.hasField(event.value, field.value, events) ||
              DomainEventDetail.isEmittedByFactoryMethod(event.value, aggregate)) {
        return "currentData." + field.value;
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

    final CodeGenerationParameter loadedEvent =
            events.stream().filter(e -> e.value.equals(event.value)).findFirst().get();

    return Formatters.Variables.format(Formatters.Variables.Style.EVENT_BASED_DATA_OBJECT_INITIALIZER,
            Dialect.findDefault(), loadedEvent, valueObjects.stream());
  }

  public String getName() {
    return name;
  }

  public String getDataObjectName() {
    return dataObjectName;
  }

  public String getMergeParameters() {
    return mergeParameters;
  }

  public List<String> getDataObjectInitializers() {
    return dataObjectInitializers;
  }
}
