// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.MethodScope;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignatureDeclaration implements Formatters.Arguments {

  private static final String SIGNATURE_PATTERN = "final %s %s";
  private static final String STAGE_ARGUMENT = String.format(SIGNATURE_PATTERN, "Stage", "stage");

  SignatureDeclaration() {
  }

  @Override
  public String format(final CodeGenerationParameter parameter, final MethodScope scope) {
    final List<String> args = scope.isStatic() ?
            Arrays.asList(STAGE_ARGUMENT) : Arrays.asList();

    return Stream.of(args, collectMethodParameters(parameter))
            .flatMap(Collection::stream).collect(Collectors.joining(", "));
  }

  private List<String> collectMethodParameters(final CodeGenerationParameter parameter) {
    switch (Label.valueOf(parameter.label.toString())) {
      case AGGREGATE:
      case DOMAIN_EVENT:
        return collectStateFields(parameter);
      case ROUTE_SIGNATURE:
        return formatRouteMethodParameters(parameter);
      case AGGREGATE_METHOD:
        return formatAggregateMethodParameters(parameter);
      case VALUE_OBJECT:
        return formatValueObjectFields(parameter);
      default:
        throw new UnsupportedOperationException("Unable to format fields of " + parameter.label);
    }
  }

  private List<String> collectStateFields(final CodeGenerationParameter aggregate) {
    return applyAggregateBasedFormatting(aggregate, Label.STATE_FIELD);
  }

  private List<String> formatRouteMethodParameters(final CodeGenerationParameter route) {
    return applyAggregateBasedFormatting(route, Label.METHOD_PARAMETER);
  }

  private List<String> formatAggregateMethodParameters(final CodeGenerationParameter aggregateMethod) {
    return applyAggregateBasedFormatting(aggregateMethod, Label.METHOD_PARAMETER);
  }

  private List<String> applyAggregateBasedFormatting(final CodeGenerationParameter parent,
                                                     final Label relatedParameter) {
    return parent.retrieveAllRelated(relatedParameter).map(field -> {
      final String fieldType = FieldDetail.typeOf(field.parent(Label.AGGREGATE), field.value);

      if(FieldDetail.isCollection(fieldType)) {
        final CollectionMutation collectionMutation =
                field.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

        if(collectionMutation.isSingleParameterBased()) {
          return String.format(SIGNATURE_PATTERN, FieldDetail.genericTypeOf(fieldType), field.retrieveRelatedValue(Label.ALIAS));
        }
      }
      if(fieldType.equalsIgnoreCase(CodeGenerationProperties.COMPOSITE_ID_TYPE))
        return String.format(SIGNATURE_PATTERN, "String", field.value);
      else
        return String.format(SIGNATURE_PATTERN, fieldType, field.value);
    }).collect(Collectors.toList());
  }

  private List<String> formatValueObjectFields(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).map(field -> {
      final String fieldType = FieldDetail.typeOf(valueObject, field.value);
      return String.format(SIGNATURE_PATTERN, fieldType, field.value);
    }).collect(Collectors.toList());
  }

}
