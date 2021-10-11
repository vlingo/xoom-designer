package io.vlingo.xoom.cli.task.docker;

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
import java.util.List;

public class DockerPackageTaskTest {

  private CommandRetainer commandRetainer;

  @BeforeEach
  public void setUp() {
    Profile.enableTestProfile();
    Terminal.enable(Terminal.MAC_OS);
    this.commandRetainer = new CommandRetainer();
  }

  @Test
  public void testThatTaskRuns() {
    final ProjectPath projectPath = ProjectPath.from(System.getProperty("user.dir"));
    new DockerPackageTask(commandRetainer, XoomTurboProperties.load(projectPath)).run(buildArgs());
    final String[] commandsSequence = commandRetainer.retainedCommandsSequence().get(0);
    Assertions.assertEquals(Terminal.supported().initializationCommand(), commandsSequence[0]);
    Assertions.assertEquals(Terminal.supported().parameter(), commandsSequence[1]);
    Assertions.assertEquals("cd /home/projects/xoom-app && mvn clean package && docker build ./ -t xoom-app:latest", commandsSequence[2]);
  }

  @Test
  public void testThatTaskFails() {
    Assertions.assertThrows(DockerCommandException.class, () -> {
      new DockerPackageTask(commandRetainer, XoomTurboProperties.empty()).run(buildArgs());
    });
  }

  @AfterEach
  public void clear() {
    Terminal.disable();
    Profile.disableTestProfile();
  }

  private List<String> buildArgs() {
    return Arrays.asList("docker", "package", "--currentDirectory", "/home/projects/xoom-app", "--tag", "latest");
  }
}
