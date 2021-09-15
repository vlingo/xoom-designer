// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.Label.FACTORY_METHOD;
import static io.vlingo.xoom.designer.codegen.Label.METHOD_PARAMETER;

public class AggregateMethod {

  public final String name;
  public final boolean useFactory;
  public final List<String> parameters;

  public AggregateMethod(final CodeGenerationParameter method) {
    this.name = method.value;
    this.useFactory = method.hasAny(FACTORY_METHOD) ? method.retrieveRelatedValue(FACTORY_METHOD, Boolean::valueOf) : false;
    this.parameters = method.retrieveAllRelated(METHOD_PARAMETER).map(param -> param.value).collect(Collectors.toList());
  }
}
