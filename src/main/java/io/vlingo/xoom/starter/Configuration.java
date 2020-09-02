package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.media.ContentMediaType;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.content.ContentCreationStep;
import io.vlingo.xoom.codegen.template.bootstrap.BootstrapGenerationStep;
import io.vlingo.xoom.codegen.template.entitydata.EntityDataGenerationStep;
import io.vlingo.xoom.codegen.template.model.ModelGenerationStep;
import io.vlingo.xoom.codegen.template.projections.ProjectionGenerationStep;
import io.vlingo.xoom.codegen.template.resource.RestResourceGenerationStep;
import io.vlingo.xoom.codegen.template.storage.StorageGenerationStep;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.gui.steps.UserInterfaceBootstrapStep;
import io.vlingo.xoom.starter.task.steps.*;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.steps.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    public static final String USER_INTERFACE_CONFIG_KEY = "ui";
    public static final String PROPERTIES_FILENAME = "vlingo-xoom-starter.properties";
    public static final String HOME_ENVIRONMENT_VARIABLE = "VLINGO_XOOM_STARTER_HOME";
    public static final ResponseHeader GENERATION_SETTINGS_RESPONSE_HEADER =
            ResponseHeader.contentType(new ContentMediaType("application", "vnd.generationsettings+json").toString());

    public static final Map<Terminal, String> BROWSER_LAUNCH_COMMAND =
            Maps.immutableEnumMap(
                    new HashMap<Terminal, String>() {{
                        put(Terminal.WINDOWS, "start ");
                        put(Terminal.MAC_OS, "open ");
                        put(Terminal.LINUX, "xdg-open ");
                    }}
            );

    public static final List<TaskExecutionStep> TEMPLATE_GENERATION_STEPS = Arrays.asList(
            new ResourcesLocationStep(),
            new PropertiesLoadStep(),
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
            new BootstrapGenerationStep(),
            new ContentCreationStep()
    );

    public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
            new UserInterfaceBootstrapStep(), new ApplicationConfigLoaderStep(),
            new BrowserLaunchCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

}
