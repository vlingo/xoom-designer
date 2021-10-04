// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.cli.OptionName;
import io.vlingo.xoom.designer.cli.Property;
import io.vlingo.xoom.designer.cli.TaskExecutionContext;
import io.vlingo.xoom.designer.cli.TaskExecutionStep;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class InfraResourcesInitializationStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
    resolvePort(context);
    Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
  }

  private void resolvePort(final TaskExecutionContext context) {
    if(context.hasOption(OptionName.PORT)) {
      final Integer port = Integer.valueOf(context.optionValueOf(OptionName.PORT));
      if(port > 0) {
        ComponentRegistry.register(Property.DESIGNER_SERVER_PORT.literal(), port);
      }
    }
  }

}
