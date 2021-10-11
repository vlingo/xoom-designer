package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.designer.infrastructure.BrowserLauncher;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.terminal.Terminal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrowserLauncherTest {

  private static final String EXPECTED_URL = "http://localhost:19090/context";

  @Test
  public void testBrowserLaunchCommandResolution() {
    final CommandRetainer commandRetainer = new CommandRetainer();
    new BrowserLauncher(commandRetainer).execute();
    final String[] commands = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commands[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commands[1]);
    Assertions.assertEquals(expectedLaunchCommand(), commands[2]);
  }

  private String expectedLaunchCommand() {
    final Terminal supportedTerminal = Terminal.supported();
    if (supportedTerminal.equals(Terminal.WINDOWS)) {
      return "start " + EXPECTED_URL;
    }
    if (supportedTerminal.equals(Terminal.MAC_OS)) {
      return "open " + EXPECTED_URL;
    }
    if (supportedTerminal.equals(Terminal.LINUX)) {
      return "xdg-open " + EXPECTED_URL;
    }
    throw new RuntimeException("Terminal is not supported");
  }

  @BeforeEach
  public void setUp() {
    Terminal.disable();
    Infrastructure.clear();
    Profile.enableTestProfile();
    Infrastructure.setupResources(HomeDirectory.fromEnvironment(), 9019);
  }

  @AfterAll
  public static void clear() {
    Profile.disableTestProfile();
  }
}
