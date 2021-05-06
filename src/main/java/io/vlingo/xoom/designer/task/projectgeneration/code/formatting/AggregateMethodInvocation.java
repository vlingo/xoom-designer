// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.METHOD_PARAMETER;
import static java.util.stream.Collectors.toList;

public class AggregateMethodInvocation implements Formatters.Arguments {

  private final String carrier;
  private final String stageVariableName;
  private static final String FIELD_ACCESS_PATTERN = "%s.%s";

  public AggregateMethodInvocation(final String stageVariableName) {
    this(stageVariableName, "");
  }

  public AggregateMethodInvocation(final String stageVariableName, final String carrier) {
    this.carrier = carrier;
    this.stageVariableName = stageVariableName;
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

  private String resolveFieldAccess(final CodeGenerationParameter parameter) {
    final CodeGenerationParameter stateField =
            AggregateDetail.stateFieldWithName(parameter.parent(AGGREGATE), parameter.value);

    if (carrier.isEmpty() || ValueObjectDetail.isValueObject(stateField) || FieldDetail.isValueObjectCollection(stateField)) {
      return parameter.value;
    }
    return String.format(FIELD_ACCESS_PATTERN, carrier, parameter.value);
  }

}
