package io.vlingo.xoom.starter;

import com.google.common.collect.Maps;
import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.Terminal;

import java.util.HashMap;
import java.util.Map;

import static io.vlingo.xoom.starter.codegen.CodeTemplate.*;

public class ApplicationConfiguration {

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
                        put(StorageType.OBJECT_STORE, PLAIN_STATE.filename);
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


}
