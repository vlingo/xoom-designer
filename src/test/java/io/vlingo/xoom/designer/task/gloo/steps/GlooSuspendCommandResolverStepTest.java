// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.gloo.steps;

import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlooSuspendCommandResolverStepTest {

    @Test
    public void testGlooSuspendCommandResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        new GlooSuspendCommandResolverStep().process(context);

        Assertions.assertEquals(Terminal.supported().initializationCommand(), context.commands()[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), context.commands()[1]);
        Assertions.assertEquals("glooctl uninstall gateway", context.commands()[2]);
    }

}
