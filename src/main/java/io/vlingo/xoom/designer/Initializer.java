// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.task.Task;
import io.vlingo.xoom.designer.task.TaskExecutor;

import java.util.Arrays;
import java.util.List;

public class Initializer {

  public static void main(final String[] args) {
    try {
      TaskExecutor.execute(prepareArgs(args));
    } catch (final Exception exception) {
      exception.printStackTrace();
      Logger.basicLogger().error(exception.getMessage());
    } finally {
      if (TaskExecutor.shouldExit()) {
        System.exit(0);
      }
    }
  }

  private static List<String> prepareArgs(final String[] args) {
    if(args.length  == 0) {
      return Arrays.asList(Task.resolveDefaultCommand());
    }
    return Arrays.asList(args);
  }
}
