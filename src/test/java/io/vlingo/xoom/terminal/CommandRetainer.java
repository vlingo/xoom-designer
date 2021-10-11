// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRetainer extends CommandExecutionProcess {

  private final List<String[]> retainedCommands = new ArrayList<>();

  @Override
  protected Process execute(final String[] commandSequence) {
    this.retainedCommands.add(commandSequence);
    return null;
  }

  @Override
  protected void log(final Process process) {}

  @Override
  protected void handleCommandExecutionStatus(final Process process) {}

  public List<String[]> retainedCommandsSequence() {
    return Collections.unmodifiableList(retainedCommands);
  }

}
