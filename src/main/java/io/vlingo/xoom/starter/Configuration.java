package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import io.vlingo.xoom.codegen.steps.*;
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
            new ArchetypeCommandResolverStep(),
            new CommandExecutionStep(),
            new LoggingStep(),
            new StatusHandlingStep(),
            // TemplateProcessingSteps starts here
            CodeGenerationStepAdapter.of(new ModelGenerationStep()),
            CodeGenerationStepAdapter.of(new ProjectionGenerationStep()),
            CodeGenerationStepAdapter.of(new StorageGenerationStep()),
            CodeGenerationStepAdapter.of(new RestResourceGenerationStep()),
            CodeGenerationStepAdapter.of(new BootstrapGenerationStep()),
            // TemplateProcessingSteps ends here
            CodeGenerationStepAdapter.of(new ContentCreationStep()),
            new ContentPurgerStep()
    );

    public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
            new UserInterfaceBootstrapStep(), new ApplicationConfigLoaderStep(),
            new BrowserLaunchCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

}

