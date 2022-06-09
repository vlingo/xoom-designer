// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.MethodScope;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateMethodInvocation implements Formatters.Arguments {

  private final String stageVariableName;
  private final ParametersOwner parametersOwner;
  private static final String FIELD_ACCESS_PATTERN = "%s.%s";
  private static final String SCALAR_TYPED_SINGLETON_COLLECTION_PATTERN = "%s.%s.stream().findFirst().orElse(null)";
  private CodeGenerationParameter valueObject;

  public AggregateMethodInvocation(String stageVariableName, ParametersOwner consumedEvent, CodeGenerationParameter valueObject) {
    this(stageVariableName, consumedEvent);
    this.valueObject = valueObject;
  }

  public static AggregateMethodInvocation accessingParametersFromDataObject(final String stageVariableName) {
    return new AggregateMethodInvocation(stageVariableName, ParametersOwner.DATA_OBJECT);
  }

  public static AggregateMethodInvocation accessingParametersFromConsumedEvent(final String stageVariableName) {
    return new AggregateMethodInvocation(stageVariableName, ParametersOwner.CONSUMED_EVENT);
  }

  public static AggregateMethodInvocation accessingValueObjectParametersFromConsumedEvent(String stageVariableName,
                                                                                          CodeGenerationParameter valueObject) {
    return new AggregateMethodInvocation(stageVariableName, ParametersOwner.CONSUMED_EVENT, valueObject);
  }

  public AggregateMethodInvocation(final String stageVariableName) {
    this(stageVariableName, ParametersOwner.NONE);
  }

  public AggregateMethodInvocation(final String stageVariableName, final ParametersOwner parametersOwner) {
    this.stageVariableName = stageVariableName;
    this.parametersOwner = parametersOwner;
  }

  @Override
  public String format(final CodeGenerationParameter method, final MethodScope scope) {
    final List<String> args = scope.isStatic() ? Collections.singletonList(stageVariableName) : Collections.emptyList();
    if (valueObject != null) {
      String collect = Stream.of(getCollect(formatMethodParameters(method).get(0), valueObject, scope))
              .flatMap(Collection::stream)
              .collect(Collectors.joining(", "));
      if(scope.isStatic())
        return String.format("%s, %s.from(%s)", stageVariableName, valueObject.value, collect);

      return String.format("%s.from(%s)", valueObject.value, collect);
    } else
      return Stream.of(args, formatMethodParameters(method))
              .flatMap(Collection::stream)
              .collect(Collectors.joining(", "));
  }

  private List<String> getCollect(final String fieldPath, final CodeGenerationParameter valueObject, MethodScope scope) {
    return Arrays.stream(Formatters.Arguments.DATA_OBJECT_CONSTRUCTOR_INVOCATION.format(valueObject, scope).split(", "))
            .map(param -> resolveParameterFieldPathWithoutDuplication(fieldPath, param))
            .collect(toList());
  }

  private String resolveParameterFieldPathWithoutDuplication(String fieldPath, String param) {
    return Arrays.stream(String.format("%s.%s", fieldPath, param).split("\\.")).distinct().collect(Collectors.joining("."));
  }

  private List<String> formatMethodParameters(final CodeGenerationParameter method) {
    return method.retrieveAllRelated(Label.METHOD_PARAMETER).map(this::resolveFieldAccess).collect(toList());
  }

  private String resolveFieldAccess(final CodeGenerationParameter methodParameter) {
    final CodeGenerationParameter stateField =
            AggregateDetail.stateFieldWithName(methodParameter.parent(Label.AGGREGATE), methodParameter.value);

    final String fieldPath =
            parametersOwner.isNone() ? methodParameter.value : String.format(FIELD_ACCESS_PATTERN, parametersOwner.ownerName, methodParameter.value);

    if(parametersOwner.isConsumedEvent()) {
      return fieldPath;
    }

    if(parametersOwner.isDataObject() && FieldDetail.isValueObjectCollection(stateField)) {
      return ValueObjectDetail.translateDataObjectCollection(fieldPath, stateField, methodParameter);
    }

    final String fieldType =
            FieldDetail.typeOf(methodParameter.parent(Label.AGGREGATE), methodParameter.value);

    if(FieldDetail.isCollection(fieldType)) {
      final CollectionMutation collectionMutation =
              methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

      if(collectionMutation.isSingleParameterBased()) {
        if(parametersOwner.isDataObject()) {
          return String.format(SCALAR_TYPED_SINGLETON_COLLECTION_PATTERN, parametersOwner.ownerName, methodParameter.value);
        } else {
          return methodParameter.retrieveRelatedValue(Label.ALIAS);
        }
      }
    }

    if(ValueObjectDetail.isValueObject(stateField)) {
      return stateField.value;
    }

    return fieldPath;
  }



  private enum ParametersOwner {
    NONE(null),
    DATA_OBJECT("data"),
    CONSUMED_EVENT("event");

    final String ownerName;

    ParametersOwner(final String ownerName) {
      this.ownerName = ownerName;
    }

    boolean isDataObject() {
      return ParametersOwner.DATA_OBJECT.equals(this);
    }

    boolean isConsumedEvent() {
      return CONSUMED_EVENT.equals(this);
    }

    boolean isNone() {
      return NONE.equals(this);
    }
  }
}
