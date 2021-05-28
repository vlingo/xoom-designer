// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;
import static java.util.stream.Collectors.toList;

public class AggregateMethodInvocation implements Formatters.Arguments {

  private final String carrier;
  private final String stageVariableName;
  private final boolean supportDataObjectHandling;
  private static final String FIELD_ACCESS_PATTERN = "%s.%s";
  private static final String SCALAR_TYPED_SINGLETON_COLLECTION_PATTERN = "%s.%s.get(0)";

  public static AggregateMethodInvocation handlingDataObject(final String stageVariableName) {
    return new AggregateMethodInvocation(stageVariableName, "data", true);
  }

  public AggregateMethodInvocation(final String stageVariableName) {
    this(stageVariableName, "", false);
  }

  public AggregateMethodInvocation(final String stageVariableName,
                                   final String carrier,
                                   final boolean supportDataObjectHandling) {
    this.carrier = carrier;
    this.stageVariableName = stageVariableName;
    this.supportDataObjectHandling = supportDataObjectHandling;
  }

  @Override
  public String format(final CodeGenerationParameter method, final MethodScope scope) {
    final List<String> args = scope.isStatic() ?
            Arrays.asList(stageVariableName) : Arrays.asList();

    return Stream.of(args, formatMethodParameters(method))
            .flatMap(Collection::stream).collect(Collectors.joining(", "));
  }

  private List<String> formatMethodParameters(final CodeGenerationParameter method) {
    return method.retrieveAllRelated(METHOD_PARAMETER).map(this::resolveFieldAccess).collect(toList());
  }

  private String resolveFieldAccess(final CodeGenerationParameter methodParameter) {
    final CodeGenerationParameter stateField =
            AggregateDetail.stateFieldWithName(methodParameter.parent(AGGREGATE), methodParameter.value);

    final String fieldPath =
            carrier.isEmpty() ? methodParameter.value : String.format(FIELD_ACCESS_PATTERN, carrier, methodParameter.value);

    if(supportDataObjectHandling && FieldDetail.isValueObjectCollection(stateField)) {
      return ValueObjectDetail.translateDataObjectCollection(fieldPath, stateField, methodParameter);
    }

    final String fieldType =
            FieldDetail.typeOf(methodParameter.parent(AGGREGATE), methodParameter.value);

    if(FieldDetail.isCollection(fieldType)) {
      final CollectionMutation collectionMutation =
              methodParameter.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

      if(collectionMutation.isSingleParameterBased()) {
        if(supportDataObjectHandling) {
          return String.format(SCALAR_TYPED_SINGLETON_COLLECTION_PATTERN, carrier, methodParameter.retrieveRelatedValue(ALIAS));
        } else {
          return methodParameter.retrieveRelatedValue(ALIAS);
        }
      }
    }

    if(ValueObjectDetail.isValueObject(stateField)) {
      return stateField.value;
    }

    return fieldPath;
  }

}
