// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.version;

import io.vlingo.xoom.cli.task.Task;

import java.util.List;

public class VersionDisplayTask extends Task {

  public VersionDisplayTask() {
    super("-version");
  }

  @Override
  public void run(final List<String> args) {
    System.out.println(getClass().getPackage().getImplementationVersion());
  }
}
