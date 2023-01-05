// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.terminal.CommandRetainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandLineInterfaceInitializerTest {

  @BeforeEach
  public void setUp() {
    ComponentsRegistration.registerWith(Logger.noOpLogger(), new CommandRetainer(), XoomTurboProperties.empty());
  }

  @Test
  public void testThatCommandIsResolved() {
    Assertions.assertEquals("designer", CommandLineInterfaceInitializer.resolveCommand(new String[]{}));
    Assertions.assertEquals("designer", CommandLineInterfaceInitializer.resolveCommand(new String[]{"designer"}));
    Assertions.assertEquals("designer", CommandLineInterfaceInitializer.resolveCommand(new String[]{"designer", "--target", "filesystem", "--profile", "test"}));
    Assertions.assertEquals("docker package", CommandLineInterfaceInitializer.resolveCommand(new String[]{"docker", "package"}));
    Assertions.assertEquals("docker package", CommandLineInterfaceInitializer.resolveCommand(new String[]{"docker", "package", "--tag", "latest"}));
  }

}
