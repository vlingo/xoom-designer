// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PreliminaryStatement {

  private static final String DISPATCHER_AFTER_COMPLETION = "final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);";

  public static final List<String> resolve(final CodeGenerationParameter method,
                                           final Optional<String> defaultFactoryMethodName) {
    if(method.hasAny(Label.DOMAIN_EVENT)) {
      if (AuxiliaryEntityCreation.isRequiredFor(method, defaultFactoryMethodName)) {
        final String entityCreationMethodInvocation = AuxiliaryEntityCreation.METHOD_NAME + "();";
        return Arrays.asList(entityCreationMethodInvocation, DISPATCHER_AFTER_COMPLETION);
      }
      return Arrays.asList(DISPATCHER_AFTER_COMPLETION);
    } else {
      return Collections.emptyList();
    }
  }

}
