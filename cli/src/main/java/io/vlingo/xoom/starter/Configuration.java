package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.gui.steps.GraphicalUserInterfaceBootstrapStep;
import io.vlingo.xoom.starter.task.steps.*;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.steps.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.vlingo.xoom.starter.codegeneration.CodeTemplate.*;

public class Configuration {

    public static final String USER_INTERFACE_CONFIG_KEY = "ui";

    public static final Map<StorageType, String> AGGREGATE_TEMPLATES =
            Maps.immutableEnumMap(
                new HashMap<StorageType, String>(){{
                    put(StorageType.OBJECT_STORE, OBJECT_ENTITY.filename);
                    put(StorageType.STATE_STORE, STATEFUL_ENTITY.filename);
                    put(StorageType.JOURNAL, EVENT_SOURCE_ENTITY.filename);
                }}
            );

    public static final Map<StorageType, String> STATE_TEMPLATES =
            Maps.immutableEnumMap(
                new HashMap<StorageType, String>(){{
                    put(StorageType.OBJECT_STORE, STATE_OBJECT.filename);
                    put(StorageType.STATE_STORE, STATE_OBJECT.filename);
                    put(StorageType.JOURNAL, PLAIN_STATE.filename);
                }}
            );

    public static final Map<Terminal, String> BROWSER_LAUNCH_COMMAND =
            Maps.immutableEnumMap(
                new HashMap<Terminal, String>() {{
                    put(Terminal.WINDOWS, "start ");
                    put(Terminal.MAC_OS, "open ");
                    put(Terminal.LINUX, "xdg-open ");
                }}
            );

    public static final List<TaskExecutionStep> TEMPLATE_GENERATION_STEPS = Arrays.asList(
            new ResourcesLocationStep(), new PropertiesLoadStep(),
            new ArchetypeCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep(), new ModelCodeGenerationStep(),
            new RestResourceGenerationStep(), new OutputResourceCreationStep(),
            new ContentPurgerStep()
    );

    public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
            new GraphicalUserInterfaceBootstrapStep(), new ApplicationConfigLoaderStep(),
            new BrowserLaunchCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

    public static final String PROPERTIES_FILENAME = "vlingo-xoom-starter.properties";
}
