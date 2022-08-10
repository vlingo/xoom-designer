// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class ResultAssignmentStatement {

  public static String resolve(final CodeGenerationParameter aggregate, final CodeGenerationParameter method) {

    final String entityMethodInvocation = resolveEntityMethodInvocation(aggregate, method);

    if(method.hasAny(Label.METHOD_PARAMETER)) {
      return String.format("var state = %s", entityMethodInvocation);
    }
    return String.format("%s", entityMethodInvocation);
  }

  public static String resolveEntityMethodInvocation(final CodeGenerationParameter aggregate, final CodeGenerationParameter method) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");

    final String variableName =  "_" + codeElementFormatter.simpleNameToAttribute(aggregate.value);

    final String methodParameters = method.retrieveAllRelated(Label.METHOD_PARAMETER)
        .map(param -> TestDataFormatter.formatStaticVariableName(method, param))
        .collect(Collectors.joining(", "));

    return String.format("%s.%s(%s).Await();", variableName, toPascalCase(method.value), methodParameters);
  }

}
