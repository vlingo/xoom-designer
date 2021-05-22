// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

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
    this.path = route.retrieveRelatedValue(ROUTE_PATH);
    this.methodName = route.retrieveRelatedValue(ROUTE_SIGNATURE);
    this.httpMethod = route.retrieveRelatedValue(ROUTE_METHOD);
  }
}
