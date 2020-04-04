package io.vlingo.xoom.starter.archetype;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.vlingo.xoom.starter.archetype.ArchetypeProperties.*;
import static java.io.File.separator;

public enum Archetype {

    BASIC("basic-archetype","vlingo-xoom-basic-archetype",
            "io.vlingo", "1.0", new BasicArchetypePropertiesValidator(),
            VERSION, GROUP_ID, ARTIFACT_ID, PACKAGE, TARGET_FOLDER,
            XOOM_SERVER_VERSION),

    KUBERNETES("kubernetes-archetype", "vlingo-xoom-kubernetes-archetype",
            "io.vlingo", "1.0", new KubernetesArchetypePropertiesValidator(),
            VERSION, GROUP_ID, ARTIFACT_ID, PACKAGE, TARGET_FOLDER, XOOM_SERVER_VERSION,
            DEPLOYMENT, KUBERNETES_POD_NAME, KUBERNETES_IMAGE);

    private final String label;
    private final String artifactId;
    private final String groupId;
    private final String version;
    private final ArchetypePropertiesValidator validator;
    private final List<ArchetypeProperties> requiredProperties = new ArrayList<>();

    private static final String ROOT_FOLDER_KEY = "user.dir";
    private static final String ARCHETYPE_FOLDER_PATTERN= "%s%sresources%sarchetypes%s%s";

    Archetype(final String label,
              final String artifactId,
              final String groupId,
              final String version,
              final ArchetypePropertiesValidator validator,
              final ArchetypeProperties... requiredProperties) {
        this.label = label;
        this.artifactId = artifactId;
        this.groupId = groupId;
        this.version = version;
        this.validator = validator;
        this.requiredProperties.addAll(Arrays.asList(requiredProperties));
    }

    public static Archetype support(final Properties properties) {
        return Arrays.asList(Archetype.values()).stream()
                .filter(archetype -> archetype.isSupported(properties))
                .findFirst().orElseThrow(ArchetypeNotFoundException::new);
    }

    private boolean isSupported(final Properties properties) {
        return validator.validate(properties, requiredProperties);
    }

    public String fillMavenOptions(final Properties properties) {
        return ArchetypeMavenOptionsBuilder.instance().build(this, properties);
    }

    public String folder() {
        return String.format(ARCHETYPE_FOLDER_PATTERN, System.getProperty(ROOT_FOLDER_KEY),
                separator, separator, separator, label);
    }

    public List<ArchetypeProperties> supportedMavenProperties() {
        return this.requiredProperties.stream()
                .filter(properties -> properties.isSupportedByMaven())
                .collect(Collectors.toList());
    }

    String artifactId() {
        return artifactId;
    }

    String groupId() {
        return groupId;
    }

    String version() {
        return version;
    }

}