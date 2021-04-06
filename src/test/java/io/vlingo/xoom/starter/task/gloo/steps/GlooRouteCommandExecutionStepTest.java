// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.gloo.steps;

import io.vlingo.xoom.starter.infrastructure.terminal.CommandRetainer;
import io.vlingo.xoom.starter.infrastructure.terminal.Terminal;
import io.vlingo.xoom.starter.task.Property;
import io.vlingo.xoom.starter.task.TaskExecutionContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class GlooRouteCommandExecutionStepTest {

    private static final String EXPECTED_COMMAND =
            "glooctl add route --path-exact /account/type --dest-name default-banking-8080 --prefix-rewrite /v1/account/type && " +
            "glooctl add route --path-exact /balance --dest-name default-banking-8080 --prefix-rewrite /v1/balance && " +
            "glooctl add route --path-exact /account --dest-name default-banking-8080 --prefix-rewrite /v1/account";

    @Test
    public void testGlooRouteCommandResolution() {
        final TaskExecutionContext context =
                TaskExecutionContext.withoutOptions();

        final Properties properties = new Properties();
        properties.put(Property.GLOO_UPSTREAM.literal(), "default-banking-8080");
        properties.put("gloo.resource.balance", "v1/balance");
        properties.put("gloo.resource.account", "v1/account");
        properties.put("gloo.resource.account.type", "v1/account/type");
        properties.put("gloo.gateway.balance", "balance");
        properties.put("gloo.gateway.account", "account");
        properties.put("gloo.gateway.account.type", "account/type");

        context.onProperties(properties);

        final CommandRetainer commandRetainer = new CommandRetainer();
        new GlooRouteCommandExecutionStep(commandRetainer).process(context);

        final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
        Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
        Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
        Assertions.assertEquals(EXPECTED_COMMAND, commandSequence[2]);
    }
}
