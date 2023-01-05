// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail;

import java.util.stream.Stream;

public enum MethodScope {

  INSTANCE(Completes.class),
  STATIC(Completes.class, Definition.class, Stage.class);

  public final Class<?>[] requiredClasses;

  MethodScope(final Class<?>... requiredClasses) {
    this.requiredClasses = requiredClasses;
  }

  public boolean isStatic() {
    return equals(STATIC);
  }

  public boolean isInstance() {
    return equals(INSTANCE);
  }

  public static Stream<MethodScope> infer(final CodeGenerationParameter method) {
    if (method.retrieveRelatedValue(Label.FACTORY_METHOD, Boolean::valueOf) || !AggregateDetail.hasFactoryMethod(method.parent())) {
      return Stream.of(values());
    }
    return Stream.of(INSTANCE);
  }


}
