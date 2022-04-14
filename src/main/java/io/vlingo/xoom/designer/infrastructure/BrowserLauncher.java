// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;
import io.vlingo.xoom.terminal.Terminal;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class BrowserLauncher extends CommandExecutor {


  public BrowserLauncher(final CommandExecutionProcess commandExecutionProcess) {
    super(commandExecutionProcess);
  }

  @Override
  protected String formatCommands() {
    final String browserLaunchCommand = Terminal.supported().browserLaunchCommand();
    final DesignerServerConfiguration designerServerConfiguration = ComponentRegistry.withType(DesignerServerConfiguration.class);
    return String.format("%s %s", browserLaunchCommand, designerServerConfiguration.resolveUserInterfaceURL());
  }

}
