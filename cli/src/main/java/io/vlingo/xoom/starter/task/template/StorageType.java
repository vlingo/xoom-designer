package io.vlingo.xoom.starter.task.template;

public enum StorageType {

    JOURNAL("JOURNAL", "Journal", "SourcedTypeRegistry", "io.vlingo.lattice.model.sourcing"),
    OBJECT_STORE("OBJECT_STORE", "ObjectStore", "ObjectTypeRegistry", "io.vlingo.lattice.model.object"),
    STATE_STORE("STATE_STORE", "StateStore", "StatefulTypeRegistry", "io.vlingo.lattice.model.stateful");

    public final String key;
    public final String title;
    public final String registryClassName;
    private final String registryPackage;

    StorageType(final String key,
                final String title,
                final String registryClassName,
                final String registryPackage) {
        this.key = key;
        this.title = title;
        this.registryClassName = registryClassName;
        this.registryPackage = registryPackage;
    }

    public static StorageType of(final String storage) {
        return valueOf(storage.toUpperCase());
    }

    public String registryQualifiedClassName() {
        return registryPackage + "." + registryClassName;
    }

}
