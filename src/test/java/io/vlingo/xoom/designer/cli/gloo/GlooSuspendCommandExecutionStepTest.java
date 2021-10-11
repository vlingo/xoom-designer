// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.cli.gloo;

import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlooSuspendCommandExecutionStepTest {

    @Test
    public void testGlooSuspendCommandResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.bare();

        final CommandRetainer commandRetainer = new CommandRetainer();
//        new GlooSuspendCommandExecutionStep(commandRetainer).processTaskWith(context);

        final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
        Assertions.assertEquals("glooctl uninstall gateway", commandSequence[2]);
    }

}
