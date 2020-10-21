package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.media.ContentMediaType;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.content.ContentCreationStep;
import io.vlingo.xoom.codegen.template.autodispatch.AutoDispatchMappingGenerationStep;
import io.vlingo.xoom.codegen.template.bootstrap.BootstrapGenerationStep;
import io.vlingo.xoom.codegen.template.entitydata.EntityDataGenerationStep;
import io.vlingo.xoom.codegen.template.model.ModelGenerationStep;
import io.vlingo.xoom.codegen.template.projections.ProjectionGenerationStep;
import io.vlingo.xoom.codegen.template.resource.RestResourceGenerationStep;
import io.vlingo.xoom.codegen.template.storage.StorageGenerationStep;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.gui.steps.UserInterfaceBootstrapStep;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;
import io.vlingo.xoom.starter.task.steps.*;
import io.vlingo.xoom.starter.task.projectgeneration.Terminal;
import io.vlingo.xoom.starter.task.projectgeneration.steps.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static io.vlingo.xoom.starter.Resource.STARTER_PROPERTIES_FILE;

public class Configuration {

    public static final String USER_INTERFACE_CONFIG_KEY = "ui";
    public static final String PROPERTIES_FILENAME = "vlingo-xoom-starter.properties";
    public static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_STARTER_HOME";

    public static final Map<Terminal, String> BROWSER_LAUNCH_COMMAND =
            Maps.immutableEnumMap(
                    new HashMap<Terminal, String>() {{
                        put(Terminal.WINDOWS, "start ");
                        put(Terminal.MAC_OS, "open ");
                        put(Terminal.LINUX, "xdg-open ");
                    }}
            );

    public static final List<TaskExecutionStep> PROJECT_GENERATION_STEPS = Arrays.asList(
            new ResourcesLocationStep(),
            new CodeGenerationParametersLoadStep(),
            new MainClassResolverStep(),
            new ArchetypeCommandResolverStep(),
            new CommandExecutionStep(),
            new LoggingStep(),
            new StatusHandlingStep(),
            new CodeGenerationExecutionerStep(),
            new ContentPurgerStep()
    );

    public static final List<CodeGenerationStep> CODE_GENERATION_STEPS = Arrays.asList(
            new ModelGenerationStep(),
            new EntityDataGenerationStep(),
            new ProjectionGenerationStep(),
            new StorageGenerationStep(),
            new RestResourceGenerationStep(),
            new AutoDispatchMappingGenerationStep(),
            new BootstrapGenerationStep(),
            new ContentCreationStep()
    );

    public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
            new UserInterfaceBootstrapStep(), new ApplicationConfigLoaderStep(),
            new BrowserLaunchCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

    public static Properties loadProperties() {
        try {
            final Properties properties = new Properties();
            final File propertiesFile = new File(STARTER_PROPERTIES_FILE.path());
            properties.load(new FileInputStream(propertiesFile));
            return properties;
        } catch (final IOException e) {
            throw new ProjectGenerationException(e);
        }
    }

}
