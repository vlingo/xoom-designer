// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.terminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRetainer extends CommandExecutionProcess {

  private final List<String[]> retainedCommands = new ArrayList<>();

  @Override
  protected void execute(final String[] commandSequence) {
    this.retainedCommands.add(commandSequence);
  }

  @Override
  protected void log() {}

  @Override
  protected void handleCommandExecutionStatus() {}

  public List<String[]> retainedCommandsSequence() {
    return Collections.unmodifiableList(retainedCommands);
  }

}
