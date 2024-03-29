package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.ComponentsRegistration;
import io.vlingo.xoom.cli.UnknownCommandException;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.cli.XoomTurboProperties.ProjectPath;
import io.vlingo.xoom.cli.task.designer.DesignerTask;
import io.vlingo.xoom.cli.task.docker.DockerPackageTask;
import io.vlingo.xoom.cli.task.docker.DockerPushTask;
import io.vlingo.xoom.cli.task.docker.DockerStatusTask;
import io.vlingo.xoom.cli.task.gloo.GlooInitTask;
import io.vlingo.xoom.cli.task.gloo.GlooRouteTask;
import io.vlingo.xoom.cli.task.gloo.GlooSuspendTask;
import io.vlingo.xoom.cli.task.k8s.KubernetesPushTask;
import io.vlingo.xoom.cli.task.version.VersionDisplayTask;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.terminal.CommandRetainer;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {

  @BeforeEach
  public void setUp() {
    Profile.enableTestProfile();
    final ProjectPath projectPath = ProjectPath.from(System.getProperty("user.dir"));
    final XoomTurboProperties xoomTurboProperties = XoomTurboProperties.load(projectPath);
    ComponentsRegistration.registerWith(Logger.noOpLogger(), new CommandRetainer(), xoomTurboProperties);
  }

  @Test
  public void testThatTasksAreFoundByCommand() {
    Assertions.assertEquals(DesignerTask.class, Task.triggeredBy("gui").getClass());
    Assertions.assertEquals(DesignerTask.class, Task.triggeredBy("designer").getClass());
    Assertions.assertEquals(DockerPushTask.class, Task.triggeredBy("docker push").getClass());
    Assertions.assertEquals(DockerPackageTask.class, Task.triggeredBy("docker package").getClass());
    Assertions.assertEquals(DockerStatusTask.class, Task.triggeredBy("docker status").getClass());
    Assertions.assertEquals(GlooInitTask.class, Task.triggeredBy("gloo init").getClass());
    Assertions.assertEquals(GlooRouteTask.class, Task.triggeredBy("gloo route").getClass());
    Assertions.assertEquals(GlooSuspendTask.class, Task.triggeredBy("gloo suspend").getClass());
    Assertions.assertEquals(KubernetesPushTask.class, Task.triggeredBy("k8s push").getClass());
    Assertions.assertEquals(VersionDisplayTask.class, Task.triggeredBy("-version").getClass());
    Assertions.assertThrows(UnknownCommandException.class, () -> Task.triggeredBy("start"));
  }

  @Test
  public void testThatDefaultCommandIsResolved() {
    Assertions.assertEquals("designer", Task.resolveDefaultCommand());
  }

  @AfterEach
  public void tearDown() {
    Profile.disableTestProfile();
    ComponentRegistry.clear();
  }
}
