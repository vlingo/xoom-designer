// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GlooSuspendTaskTest {

  @Test
  public void testGlooSuspendCommandResolution() {
    final CommandRetainer commandRetainer = new CommandRetainer();
    new GlooSuspendTask(commandRetainer).run(Arrays.asList("gloo", "suspend"));
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals("glooctl uninstall gateway", commandSequence[2]);
  }

}
