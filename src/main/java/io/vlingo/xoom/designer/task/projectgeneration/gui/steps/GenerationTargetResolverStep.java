// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.gui.steps;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.option.OptionName;
import io.vlingo.xoom.designer.task.steps.TaskExecutionStep;

import static io.vlingo.xoom.designer.Configuration.XOOM_DESIGNER_GENERATION_TARGET;

public class GenerationTargetResolverStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
    final String value = context.optionValueOf(OptionName.TARGET).trim();
    if(!value.isEmpty()) {
      if(isValid(value)) {
        System.setProperty(XOOM_DESIGNER_GENERATION_TARGET, value.toLowerCase());
      } else {
        System.out.println(value + " is a invalid value for " + XOOM_DESIGNER_GENERATION_TARGET + " property.");
      }
    }
  }

  private boolean isValid(final String value) {
    return value.equalsIgnoreCase(Configuration.XOOM_DESIGNER_GENERATION_TARGET_FS) ||
            value.equalsIgnoreCase(Configuration.XOOM_DESIGNER_GENERATION_TARGET_ZIP);
  }
}
