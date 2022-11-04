// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.resource;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.AggregateDetail;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.formatting.AggregateMethodInvocation;
import io.vlingo.xoom.designer.codegen.csharp.formatting.Formatters;
import io.vlingo.xoom.http.Method;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultHandlerInvocationResolver implements HandlerInvocationResolver {

  private final static String COMMAND_PATTERN = "%s.%s(%s)";
  private final static String QUERY_PATTERN = QUERIES_PARAMETER + ".%s(%s)";
  private final static String ADAPTER_PATTERN = "%s.From(state)";

  @Override
  public String resolveRouteHandlerInvocation(final CodeGenerationParameter aggregate, final CodeGenerationParameter route) {
    if (route.retrieveRelatedValue(Label.ROUTE_METHOD, Method::from).isGET()) {
      return resolveQueryMethodInvocation(route);
    }
    return resolveCommandMethodInvocation(aggregate, route);
  }

  @Override
  public String resolveAdapterHandlerInvocation(final CodeGenerationParameter aggregateParameter,
                                                final CodeGenerationParameter routeSignatureParameter) {
    return String.format(ADAPTER_PATTERN, CsharpTemplateStandard.DATA_OBJECT.resolveClassname(aggregateParameter.value));
  }

  private String resolveCommandMethodInvocation(final CodeGenerationParameter aggregateParameter,
                                                final CodeGenerationParameter routeParameter) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final Formatters.Arguments argumentsFormat = AggregateMethodInvocation.accessingParametersFromDataObject("_grid");
    final CodeGenerationParameter method = AggregateDetail.methodWithName(aggregateParameter, routeParameter.value);
    final boolean factoryMethod = method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf) || !AggregateDetail.hasFactoryMethod(aggregateParameter);
    final MethodScope scope = factoryMethod ? MethodScope.STATIC : MethodScope.INSTANCE;
    final String methodInvocationParameters = argumentsFormat.format(method, scope);
    final String invoker = factoryMethod ? "I"+aggregateParameter.value : codeElementFormatter.simpleNameToAttribute(aggregateParameter.value);
    return String.format(COMMAND_PATTERN, invoker, method.value, methodInvocationParameters);
  }

  private String resolveQueryMethodInvocation(final CodeGenerationParameter route) {
    final String arguments = Formatters.Arguments.QUERIES_METHOD_INVOCATION.format(route);
    final String parameters = formatParameters(Stream.of(arguments));
    return String.format(QUERY_PATTERN, route.value, parameters);
  }

  public static String formatParameters(Stream<String> arguments) {
    return arguments
        .distinct()
        .filter(param -> !param.isEmpty())
        .collect(Collectors.joining(", "));
  }
}
