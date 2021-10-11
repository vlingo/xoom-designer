package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.cli.option.OptionValue;
import io.vlingo.xoom.cli.task.TaskExecutionContext;
import io.vlingo.xoom.cli.XoomTurboProperties;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;

public class XoomTurboPropertiesLoadStepTest {

  @Test
  public void testDockerSettingsLoad() {
    final String propertiesAbsolutePath =
            Paths.get(System.getProperty("user.dir")).toString();

    final OptionValue currentDirectory =
            OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

    final TaskExecutionContext context =
            TaskExecutionContext.withOptions(Collections.singletonList(currentDirectory));

//    new XoomTurboPropertiesLoadStep().processTaskWith(context);

    Assertions.assertEquals("xoom-app", context.propertyOf(XoomTurboProperties.DOCKER_IMAGE));
    Assertions.assertEquals("vlingo/xoom-app", context.propertyOf(XoomTurboProperties.DOCKER_REPOSITORY));
  }

  @Test
  public void testMissingDockerSettings() {
    final String propertiesAbsolutePath =
            Paths.get(System.getProperty("user.dir"), "invalid-directory").toString();

    final OptionValue currentDirectory =
            OptionValue.with(CURRENT_DIRECTORY, propertiesAbsolutePath);

    final TaskExecutionContext context =
            TaskExecutionContext.withOptions(Collections.singletonList(currentDirectory));

//    Assertions.assertThrows(ResourceLoadException.class, () -> new XoomTurboPropertiesLoadStep().processTaskWith(context));
  }

  @BeforeEach
  public void setUp() {
    Profile.enableTestProfile();
    Infrastructure.clear();
  }

  @BeforeEach
  public void clear() {
    Profile.disableTestProfile();
  }
}
