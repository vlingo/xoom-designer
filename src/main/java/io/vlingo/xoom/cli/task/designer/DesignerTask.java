// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.designer;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.designer.DesignerInitializer;
import io.vlingo.xoom.terminal.CommandExecutionProcess;

import java.util.List;

import static io.vlingo.xoom.cli.option.OptionName.*;

public class DesignerTask extends Task {

  private final DesignerInitializer initializer;

  public DesignerTask(final CommandExecutionProcess commandExecutionProcess) {
    super("designer", "gui", Option.of(PORT, "0"), Option.of(PROFILE, "PRODUCTION"), Option.of(TARGET, "FILESYSTEM"));
    this.initializer = new DesignerInitializer(commandExecutionProcess);
  }

  @Override
  public void run(final List<String> args) {
    this.initializer.start(OptionValue.mapValues(options, args));
  }

  @Override
  public boolean isDefault() {
    return true;
  }

  @Override
  public boolean shouldAutomaticallyExit() {
    return false;
  }

}

