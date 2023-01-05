// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task.k8s;

import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.Arrays;

public class KubernetesPushTaskTest {

  private CommandRetainer commandRetainer;

  @BeforeEach
  public void setUp() {
    this.commandRetainer = new CommandRetainer();
  }

  @Test
  @EnabledOnOs({OS.MAC, OS.LINUX})
  public void testThatTaskRuns() {
    new KubernetesPushTask(commandRetainer).run(Arrays.asList("k8s", "push", "--currentDirectory", "/home/projects/xoom-app"));
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals("cd /home/projects/xoom-app && kubectl apply -f deployment/k8s", commandSequence[2]);
  }

  @Test
  @EnabledOnOs({OS.WINDOWS})
  public void testThatTaskRunsOnWindows() {
    new KubernetesPushTask(commandRetainer).run(Arrays.asList("k8s", "push", "--currentDirectory", "D:\\projects\\xoom-app"));
    final String[] commandSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandSequence[1]);
    Assertions.assertEquals("D: && cd D:\\projects\\xoom-app && kubectl apply -f deployment\\k8s", commandSequence[2]);
  }

}
