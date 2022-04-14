// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.option.OptionName;
import io.vlingo.xoom.cli.task.Task;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.terminal.CommandExecutor;

import java.util.List;

public class GlooRouteTask extends Task {

  private final CommandExecutor commandExecutor;

  public GlooRouteTask(final CommandExecutionProcess commandExecutionProcess,
                       final XoomTurboProperties xoomTurboProperties) {
    super("gloo route", Option.required(OptionName.CURRENT_DIRECTORY));
    this.commandExecutor = new GlooRouteCommandExecutor(commandExecutionProcess, xoomTurboProperties);
  }

  @Override
  public void run(final List<String> args) {
    this.commandExecutor.execute();
  }

}
