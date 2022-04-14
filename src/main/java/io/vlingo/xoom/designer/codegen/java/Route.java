// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.java.resource.PathFormatter;

import static io.vlingo.xoom.designer.codegen.Label.*;

public class Route {

  public final String path;
  public final String methodName;
  public final String httpMethod;

  public Route() {
    this.path = "";
    this.methodName = "";
    this.httpMethod = "";
  }

  public Route(final CodeGenerationParameter route) {
    final String routePath = route.retrieveRelatedValue(ROUTE_PATH);
    final String rootPath = route.parent().retrieveRelatedValue(URI_ROOT);
    this.path = PathFormatter.formatAbsoluteRoutePath(rootPath, routePath);
    this.httpMethod = route.retrieveRelatedValue(ROUTE_METHOD);
    this.methodName = route.value;
  }
}
