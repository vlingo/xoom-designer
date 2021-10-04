// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.cli.OptionName;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;

public class ProfileActivationStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
    final String profileName = context.optionValueOf(OptionName.PROFILE);
    if(profileName.equalsIgnoreCase(Profile.TEST.name())) {
      context.logger().info("Enabling Test profile");
      Profile.enableTestProfile();
    }
  }

}
