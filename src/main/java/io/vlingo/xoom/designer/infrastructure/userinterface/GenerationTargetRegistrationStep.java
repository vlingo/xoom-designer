// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.task.TaskExecutionStep;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class GenerationTargetRegistrationStep implements TaskExecutionStep {

  @Override
  public void processTaskWith(final TaskExecutionContext context) {
    final String targetOption = context.optionValueOf(OptionName.TARGET);
    ComponentRegistry.register(GenerationTarget.class, GenerationTarget.from(targetOption));
  }

}
