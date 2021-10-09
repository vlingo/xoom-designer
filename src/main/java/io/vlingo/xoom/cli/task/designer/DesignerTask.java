// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.designer;

import io.vlingo.xoom.cli.option.Option;
import io.vlingo.xoom.cli.task.CLITask;

import java.util.List;

public class DesignerTask extends CLITask {

  protected DesignerTask(final String command,
                         final String alternativeCommand,
                         final List<Option> options) {
    super("designer", "gui", options);
  }

  @Override
  public void run() {

  }

}
