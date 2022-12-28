// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.resource;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

public interface HandlerInvocationResolver {

  String QUERIES_PARAMETER = "_queries";

  static HandlerInvocationResolver with() {
    return new DefaultHandlerInvocationResolver();
  }

  String resolveRouteHandlerInvocation(final CodeGenerationParameter parentParameter,
                                       final CodeGenerationParameter routeSignatureParameter);

  String resolveAdapterHandlerInvocation(final CodeGenerationParameter parentParameter,
                                         final CodeGenerationParameter routeSignatureParameter);

}
