// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.k8s;

import io.vlingo.xoom.designer.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.designer.infrastructure.terminal.Terminal;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.k8s.KubernetesPushCommandExecutionStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KubernetesPushCommandExecutionStepTest {

    private static final String LINUX_EXPECT_COMMAND = "kubectl apply -f deployment/k8s";
    private static final String WINDOWS_EXPECT_COMMAND = "kubectl apply -f deployment\\k8s";

    @Test
    public void testKubernetesPushCommandResolution() {
        final String expectedCommand =
                Terminal.supported().isWindows() ?
                        WINDOWS_EXPECT_COMMAND :
                        LINUX_EXPECT_COMMAND;

        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        final CommandRetainer commandRetainer = new CommandRetainer();

        new KubernetesPushCommandExecutionStep(commandRetainer).process(context);

        final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
        Assertions.assertEquals(expectedCommand, commandSequence[2]);
    }

}
