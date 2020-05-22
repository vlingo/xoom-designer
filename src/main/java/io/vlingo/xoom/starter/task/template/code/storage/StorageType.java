package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.DatabaseType;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;

import java.util.function.Function;

import static io.vlingo.xoom.starter.Configuration.STORAGE_DELEGATE_QUALIFIED_NAME_PATTERN;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public enum StorageType {

    JOURNAL("JOURNAL", "Journal", "SourcedTypeRegistry", "io.vlingo.lattice.model.sourcing"),

    OBJECT_STORE("OBJECT_STORE", "ObjectStore", "ObjectTypeRegistry", "io.vlingo.lattice.model.object"),

    STATE_STORE("STATE_STORE", "StateStore", "StatefulTypeRegistry", "io.vlingo.lattice.model.stateful", stateStoreParametersEnrichment());

    private static final String STORE_PROVIDER_NAME_SUFFIX = "Provider";

    public final String key;
    public final String title;
    public final String registryClassName;
    private final String registryPackage;
    private final Function<CodeTemplateParameters, CodeTemplateParameters> parametersEnrichener;

    StorageType(final String key,
                final String title,
                final String registryClassName,
                final String registryPackage) {
        this(key, title, registryClassName, registryPackage, parameters -> parameters);
    }

    StorageType(final String key,
                final String title,
                final String registryClassName,
                final String registryPackage,
                final Function<CodeTemplateParameters, CodeTemplateParameters> parametersEnrichener) {
        this.key = key;
        this.title = title;
        this.registryClassName = registryClassName;
        this.registryPackage = registryPackage;
        this.parametersEnrichener = parametersEnrichener;
    }

    public static StorageType of(final String storage) {
        return valueOf(storage.toUpperCase());
    }

    public String registryQualifiedClassName() {
        return registryPackage + "." + registryClassName;
    }

    public String resolveProviderNameFrom(final ModelClassification modelClassification) {
        final String prefix = modelClassification.isSingle() ? title : modelClassification.title + title;
        return prefix + STORE_PROVIDER_NAME_SUFFIX;
    }

    public CodeTemplateParameters enrichParameters(final CodeTemplateParameters codeTemplateParameters) {
        return parametersEnrichener.apply(codeTemplateParameters);
    }

    public static String qualifiedStoreActorNameFor(final DatabaseType databaseType, final StorageType storageType) {
        return Configuration.STORE_ACTORS.stream()
                .filter(information -> information.relateTo(databaseType, storageType))
                .findFirst().orElseThrow(() -> new StoreActorDetailNotFoundException(databaseType, storageType))
                .storeActorQualifiedName;
    }

    private static Function<CodeTemplateParameters, CodeTemplateParameters> stateStoreParametersEnrichment() {
        return parameters -> {
            final DatabaseType databaseType = parameters.find(DATABASE_TYPE);

            if(!databaseType.configurable) {
                return parameters;
            }

            final String storageDelegateClassName =
                    Configuration.STORAGE_DELEGATE_CLASS_NAME.get(databaseType);

            final String storageDelegateQualifiedClassName =
                    String.format(STORAGE_DELEGATE_QUALIFIED_NAME_PATTERN,
                            databaseType.label, storageDelegateClassName);

            return parameters.and(STORAGE_DELEGATE_NAME, storageDelegateClassName)
                    .and(CONFIGURATION_PROVIDER_NAME, databaseType.configurationProviderName())
                    .addImport(new ImportParameter(storageDelegateQualifiedClassName))
                    .addImport(new ImportParameter(databaseType.configurationProviderQualifiedName));
        };
    }

}
