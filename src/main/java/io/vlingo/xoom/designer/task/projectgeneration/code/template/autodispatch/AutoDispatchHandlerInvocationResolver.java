// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.template.autodispatch;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.resource.HandlerInvocationResolver;
import io.vlingo.xoom.http.Method;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class AutoDispatchHandlerInvocationResolver implements HandlerInvocationResolver {

  private static final String DEFAULT_ADAPTER_PARAMETER = "state";
  private static final String HANDLER_INVOCATION_PATTERN = "%s.%s";
  private static final String DEFAULT_FACTORY_METHOD_PARAMETER = "$stage";
  private static final String HANDLER_INVOCATION_WITH_DEFAULT_PARAMS_PATTERN = "%s.%s(%s)";

  public String resolveRouteHandlerInvocation(final CodeGenerationParameter parentParameter,
                                              final CodeGenerationParameter routeSignatureParameter) {
    final Method httpMethod =
            routeSignatureParameter.retrieveRelatedValue(ROUTE_METHOD, Method::from);

    final String defaultParameter = httpMethod.isGET() ? QUERIES_PARAMETER : DEFAULT_FACTORY_METHOD_PARAMETER;

    return resolve(ROUTE_HANDLER_INVOCATION, USE_CUSTOM_ROUTE_HANDLER_PARAM, defaultParameter, parentParameter, routeSignatureParameter);
  }

  public String resolveAdapterHandlerInvocation(final CodeGenerationParameter parentParameter,
                                                final CodeGenerationParameter routeSignatureParameter) {
    return resolve(ADAPTER_HANDLER_INVOCATION, USE_CUSTOM_ADAPTER_HANDLER_PARAM, DEFAULT_ADAPTER_PARAMETER, parentParameter, routeSignatureParameter);
  }

  private String resolve(final ParameterLabel invocationLabel,
                         final ParameterLabel customParamsLabel,
                         final String defaultParameter,
                         final CodeGenerationParameter parentParameter,
                         final CodeGenerationParameter routeSignatureParameter) {
    if (!routeSignatureParameter.hasAny(customParamsLabel)) {
      return "";
    }

    final String handlersConfigQualifiedName =
            parentParameter.retrieveRelatedValue(Label.HANDLERS_CONFIG_NAME);

    final String handlersConfigClassName =
            CodeElementFormatter.simpleNameOf(handlersConfigQualifiedName);

    final String invocation = routeSignatureParameter.retrieveRelatedValue(invocationLabel);

    if (routeSignatureParameter.retrieveRelatedValue(customParamsLabel, Boolean::valueOf)) {
      return String.format(HANDLER_INVOCATION_PATTERN, handlersConfigClassName, invocation);
    }

    return String.format(HANDLER_INVOCATION_WITH_DEFAULT_PARAMS_PATTERN, handlersConfigClassName, invocation, defaultParameter);
  }

}
