// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model.aggregate;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class AggregateProtocolMethodTemplateData extends TemplateData {

  private final TemplateParameters parameters;

  public static List<TemplateData> from(final TemplateParameters parentParameters,
                                        final CodeGenerationParameter aggregateParameter) {
    final Function<CodeGenerationParameter, List<TemplateData>> mapper =
        method -> fromInferredScopes(parentParameters, method);

    return aggregateParameter
        .retrieveAllRelated(Label.AGGREGATE_METHOD)
        .map(mapper)
        .flatMap(List::stream)
        .collect(toList());
  }

  private static List<TemplateData> fromInferredScopes(final TemplateParameters parentParameters,
                                                       final CodeGenerationParameter methodParameter) {
    final Function<MethodScope, TemplateData> mapper =
            scope -> new AggregateProtocolMethodTemplateData(scope, parentParameters, methodParameter);

    return MethodScope
        .infer(methodParameter)
        .map(mapper)
        .collect(toList());
  }

  private AggregateProtocolMethodTemplateData(final MethodScope methodScope, final TemplateParameters parentParameters,
                                              final CodeGenerationParameter method) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");

    this.parameters = TemplateParameters.with(TemplateParameter.METHOD_SCOPE, methodScope)
        .and(TemplateParameter.METHOD_NAME, AggregateDetail.methodNameFrom(method))
        .and(TemplateParameter.STATE_NAME, CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(method.parent().value))
        .and(TemplateParameter.ENTITY_NAME, CsharpTemplateStandard.AGGREGATE.resolveClassname(method.parent().value))
        .and(TemplateParameter.METHOD_INVOCATION_PARAMETERS, Formatters.Arguments.AGGREGATE_METHOD_INVOCATION.format(method))
        .and(TemplateParameter.METHOD_PARAMETERS, Formatters.Arguments.SIGNATURE_DECLARATION.format(method))
        .and(TemplateParameter.AGGREGATE_PROTOCOL_VARIABLE, codeElementFormatter.simpleNameToAttribute(method.parent().value))
        .and(TemplateParameter.AGGREGATE_PROTOCOL_NAME, method.parent().value);

    parentParameters.addImports(resolveImports(method, methodScope));
  }

  private Set<String> resolveImports(final CodeGenerationParameter method, final MethodScope methodScope) {
    return new HashSet<>();
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.AGGREGATE_PROTOCOL_METHOD;
  }

}
