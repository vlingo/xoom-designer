package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import freemarker.template.TemplateExceptionHandler;
import io.vlingo.xoom.starter.task.gui.steps.BrowserLaunchCommandResolverStep;
import io.vlingo.xoom.starter.task.gui.steps.GraphicalUserInterfaceBootstrapStep;
import io.vlingo.xoom.starter.task.steps.*;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.Terminal;
import io.vlingo.xoom.starter.task.template.code.infrastructure.ModelClassification;
import io.vlingo.xoom.starter.task.template.steps.*;

import java.util.*;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateFile.*;

public class Configuration {

    public static final String USER_INTERFACE_CONFIG_KEY = "ui";
    public static final String PROPERTIES_FILENAME = "vlingo-xoom-starter.properties";

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

    public static final Map<StorageType, String> STATE_ADAPTER_TEMPLATES =
            Maps.immutableEnumMap(
                new HashMap<StorageType, String>(){{
                    put(StorageType.OBJECT_STORE, "");
                    put(StorageType.STATE_STORE, STATE_ADAPTER.filename);
                    put(StorageType.JOURNAL, "");
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


    private static final Map<StorageType, String> COMMAND_MODEL_STORE_TEMPLATES =
            Maps.immutableEnumMap(
                    new HashMap<StorageType, String>(){{
                        put(StorageType.OBJECT_STORE, OBJECT_STORE_PROVIDER.filename);
                        put(StorageType.STATE_STORE, STATE_STORE_PROVIDER.filename);
                        put(StorageType.JOURNAL, JOURNAL_PROVIDER.filename);
                    }}
            );

    private static final Map<StorageType, String> QUERY_MODEL_STORE_TEMPLATES =
            Maps.immutableEnumMap(
                    new HashMap<StorageType, String>(){{
                        put(StorageType.OBJECT_STORE, QUERY_MODEL_OBJECT_STORE_PROVIDER.filename);
                        put(StorageType.STATE_STORE, QUERY_MODEL_STATE_STORE_PROVIDER.filename);
                        put(StorageType.JOURNAL, QUERY_MODEL_STATE_STORE_PROVIDER.filename);
                    }}
            );

    public static Map<StorageType, String> storeProviderTemplatesFrom(final ModelClassification modelClassification) {
        if(modelClassification.isQueryModel()) {
            return QUERY_MODEL_STORE_TEMPLATES;
        }
        return COMMAND_MODEL_STORE_TEMPLATES;
    }

    public static final List<TaskExecutionStep> TEMPLATE_GENERATION_STEPS = Arrays.asList(
            new ResourcesLocationStep(), new PropertiesLoadStep(),
            new ArchetypeCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep(), new ModelGenerationStep(),
            new InfrastructureGenerationStep(), new RestResourceGenerationStep(),
            new ContentCreationStep(), new ContentPurgerStep()
    );

    public static final List<TaskExecutionStep> GUI_STEPS = Arrays.asList(
            new GraphicalUserInterfaceBootstrapStep(), new ApplicationConfigLoaderStep(),
            new BrowserLaunchCommandResolverStep(), new CommandExecutionStep(),
            new LoggingStep(), new StatusHandlingStep()
    );

    public static freemarker.template.Configuration freeMarkerSettings() {
        final freemarker.template.Configuration configuration =
                new freemarker.template.Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

        configuration.setClassForTemplateLoading(Configuration.class, "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocale(Locale.US);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }
}
