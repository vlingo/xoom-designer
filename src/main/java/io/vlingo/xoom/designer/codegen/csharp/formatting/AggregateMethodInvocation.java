// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toCamelCase;
import static java.util.stream.Collectors.toList;

public class AggregateMethodInvocation implements Formatters.Arguments {

  private final String stageVariableName;
  private final ParametersOwner parametersOwner;
  private static final String FIELD_ACCESS_PATTERN = "%s.%s";

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

    return Stream.of(args, formatMethodParameters(method))
        .flatMap(Collection::stream)
        .collect(Collectors.joining(", "));
  }

  private List<String> formatMethodParameters(final CodeGenerationParameter method) {
    return method.retrieveAllRelated(Label.METHOD_PARAMETER).map(this::resolveFieldAccess).collect(toList());
  }

  private String resolveFieldAccess(final CodeGenerationParameter methodParameter) {
    final String parameterName = methodParameter.hasAny(Label.ALIAS) ? methodParameter.retrieveRelatedValue(Label.ALIAS) : methodParameter.value;
    return parametersOwner.isNone() ? toCamelCase(parameterName) : String.format(FIELD_ACCESS_PATTERN, parametersOwner.ownerName, toCamelCase(parameterName));
  }

  private enum ParametersOwner {
    NONE(null),
    DATA_OBJECT("data"),
    CONSUMED_EVENT("event");

    final String ownerName;

    ParametersOwner(final String ownerName) {
      this.ownerName = ownerName;
    }

    boolean isNone() {
      return NONE.equals(this);
    }
  }
}
