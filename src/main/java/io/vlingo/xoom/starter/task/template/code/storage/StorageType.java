package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateStandard.*;

public enum StorageType {

    JOURNAL("JOURNAL", "Journal", "SourcedTypeRegistry",
            "io.vlingo.lattice.model.sourcing",
            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
            "io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor",
            DOMAIN_EVENT, PLACEHOLDER_DOMAIN_EVENT),

    OBJECT_STORE("OBJECT_STORE", "ObjectStore", "ObjectTypeRegistry",
            "io.vlingo.lattice.model.object",
            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
            "io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor"),

    STATE_STORE("STATE_STORE", "StateStore", "StatefulTypeRegistry",
            "io.vlingo.lattice.model.stateful",
            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
            "io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor",
            STATE);

    private static final String STORE_PROVIDER_NAME_SUFFIX = "Provider";

    public final String key;
    public final String title;
    public final String typeRegistryClassName;
    private final String typeRegistryPackage;
    private final String defaultStoreActorQualifiedName;
    private final String inMemoryStoreActorQualifiedName;
    public final List<CodeTemplateStandard> adapterSourceClassStandards; //TODO: After ModelTemplateData refactoring, use a single parameter instead of List

    StorageType(final String key,
                final String title,
                final String typeRegistryClassName,
                final String typeRegistryPackage,
                final String defaultStoreActorQualifiedName,
                final String inMemoryStoreActorQualifiedName,
                final CodeTemplateStandard ...adapterSourceClassStandards) {
        this.key = key;
        this.title = title;
        this.typeRegistryClassName = typeRegistryClassName;
        this.typeRegistryPackage = typeRegistryPackage;
        this.defaultStoreActorQualifiedName = defaultStoreActorQualifiedName;
        this.inMemoryStoreActorQualifiedName = inMemoryStoreActorQualifiedName;
        this.adapterSourceClassStandards = Arrays.asList(adapterSourceClassStandards);
    }

    public static StorageType of(final String storage) {
        return valueOf(storage.toUpperCase());
    }

    public String resolveProviderNameFrom(final ModelClassification modelClassification) {
        final String prefix = modelClassification.isSingle() ? title : modelClassification.title + title;
        return prefix + STORE_PROVIDER_NAME_SUFFIX;
    }

    public String actorFor(final DatabaseType databaseType) {
        if(databaseType.isInMemory()) {
            return inMemoryStoreActorQualifiedName;
        }
        return defaultStoreActorQualifiedName;
    }

    public List<String> resolveTypeRegistryQualifiedNames(final Boolean useCQRS) {
        return findRelatedStorageTypes(useCQRS)
                .map(storageType -> storageType.typeRegistryQualifiedClassName())
                .collect(Collectors.toList());
    }

    public String resolveTypeRegistryObjectName(final ModelClassification modelClassification) {
        if(!modelClassification.isQueryModel()) {
            return typeRegistryObjectName();
        }
        return STATE_STORE.typeRegistryObjectName();
    }

    public Stream<StorageType> findRelatedStorageTypes(final Boolean useCQRS) {
        if(!useCQRS || isStateful()) {
            return Stream.of(this);
        }
        return Stream.of(this, STATE_STORE);
    }

    public Boolean requireAdapters(final ModelClassification modelClassification) {
        return !modelClassification.isQueryModel() || isStateful();
    }

    public Boolean isStateful() {
        return equals(STATE_STORE);
    }

    public String typeRegistryObjectName() {
        return StringUtils.uncapitalize(typeRegistryClassName);
    }

    private String typeRegistryQualifiedClassName() {
        return typeRegistryPackage + "." + typeRegistryClassName;
    }
}
