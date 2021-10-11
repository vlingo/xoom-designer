// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task.gloo;

import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.cli.XoomTurboProperties.ProjectPath;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GlooRouteTaskTest {

  private static final String EXPECTED_COMMAND =
          "glooctl add route --path-exact /account/type --dest-name default-banking-8080 --prefix-rewrite /v1/account/type && " +
                  "glooctl add route --path-exact /balance --dest-name default-banking-8080 --prefix-rewrite /v1/balance && " +
                  "glooctl add route --path-exact /account --dest-name default-banking-8080 --prefix-rewrite /v1/account";

  @BeforeEach
  public void setUp() {
    Profile.enableTestProfile();
    Terminal.enable(Terminal.MAC_OS);
  }

  @Test
  public void testThatTaskRuns() {
    final CommandRetainer commandRetainer = new CommandRetainer();
    final ProjectPath projectPath = ProjectPath.from(System.getProperty("user.dir"));
    new GlooRouteTask(commandRetainer, XoomTurboProperties.load(projectPath)).run(Arrays.asList("gloo", "route"));
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals(EXPECTED_COMMAND, commandSequence[2]);
  }

  @AfterEach
  public void clear() {
    Terminal.disable();
    Profile.disableTestProfile();
  }

}
