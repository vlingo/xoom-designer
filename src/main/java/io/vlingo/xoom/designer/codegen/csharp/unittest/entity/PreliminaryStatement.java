// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.entity;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PreliminaryStatement {

  private static final String DISPATCHER_AFTER_COMPLETION = "var dispatcherAccess = _dispatcher.AfterCompleting(1);";

  public static final List<String> resolve(final CodeGenerationParameter method, final Optional<String> defaultFactoryMethodName) {
    if(method.hasAny(Label.DOMAIN_EVENT)) {
      if (AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethodName)) {
        final String entityCreationMethodInvocation = AuxiliaryEntityCreation.METHOD_NAME + "();";
        return Arrays.asList(entityCreationMethodInvocation, DISPATCHER_AFTER_COMPLETION);
      }
      return Collections.singletonList(DISPATCHER_AFTER_COMPLETION);
    } else {
      return Collections.emptyList();
    }
  }

}
