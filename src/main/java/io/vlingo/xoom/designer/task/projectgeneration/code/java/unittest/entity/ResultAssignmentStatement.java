// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.entity;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.stream.Collectors;

public class ResultAssignmentStatement {

  public static String resolve(final CodeGenerationParameter aggregate,
                               final CodeGenerationParameter method) {
    final String aggregateState =
            JavaTemplateStandard.AGGREGATE_STATE.resolveClassname(aggregate.value);

    final String entityMethodInvocation =
            resolveEntityMethodInvocation(aggregate, method);

    if(method.hasAny(Label.METHOD_PARAMETER)) {
      return String.format("final %s state = %s", aggregateState, entityMethodInvocation);
    }
    return String.format("%s", entityMethodInvocation);
  }

  public static String resolveEntityMethodInvocation(final CodeGenerationParameter aggregate,
                                                     final CodeGenerationParameter method) {
    final CodeElementFormatter codeElementFormatter =
            ComponentRegistry.withName("defaultCodeFormatter");

    final String variableName =
            codeElementFormatter.simpleNameToAttribute(aggregate.value);

    final String methodParameters =
            method.retrieveAllRelated(Label.METHOD_PARAMETER)
                    .map(param -> TestDataFormatter.formatStaticVariableName(method, param))
                    .collect(Collectors.joining(", "));

    return String.format("%s.%s(%s).await();", variableName, method.value, methodParameters);
  }

}
