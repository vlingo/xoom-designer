package io.vlingo.xoom.starter.task.template.code;

import io.vlingo.xoom.starter.task.template.StorageType;
import io.vlingo.xoom.starter.task.template.code.storage.ModelClassification;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.starter.Configuration.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public enum CodeTemplateStandard {

    AGGREGATE_PROTOCOL(parameters -> CodeTemplateFile.AGGREGATE_PROTOCOL.filename),

    DOMAIN_EVENT(domainEventNameEnrichener(), parameters -> CodeTemplateFile.DOMAIN_EVENT.filename),

    AGGREGATE(parameters -> AGGREGATE_TEMPLATES.get(parameters.find(STORAGE_TYPE)),
            (name, parameters) -> name + "Entity"),

    STATE(parameters -> STATE_TEMPLATES.get(parameters.find(STORAGE_TYPE)),
            (name, parameters) -> name + "State"),

    ENTITY_DATA(parameters -> CodeTemplateFile.ENTITY_DATA.filename,
            (name, parameters) -> name + "Data"),

    REST_RESOURCE(parameters -> CodeTemplateFile.REST_RESOURCE.filename,
            (name, parameters) -> name + "Resource"),

    STATE_ADAPTER(parameters -> STATE_ADAPTER_TEMPLATES.get(parameters.find(STORAGE_TYPE)),
            (name, parameters) -> name + "StateAdapter"),

    STORE_PROVIDER_CONFIGURATION(parameters -> CodeTemplateFile.STORE_PROVIDER_CONFIGURATION.filename,
            (name, parameters) -> "StoreProviderConfiguration"),

    PLACEHOLDER_DOMAIN_EVENT(domainEventNameEnrichener(),
            parameters -> CodeTemplateFile.PLACEHOLDER_DOMAIN_EVENT.filename,
            (name, parameters) -> name + "PlaceholderDefined"),

    PROJECTION(parameters -> PROJECTION_TEMPLATES.get(parameters.find(PROJECTION_TYPE)),
            (name, parameters) -> name + "ProjectionActor"),

    PROJECTION_DISPATCHER_PROVIDER(parameters -> CodeTemplateFile.PROJECTION_DISPATCHER_PROVIDER.filename,
            (name, parameters) -> "ProjectionDispatcherProvider"),

    STORE_PROVIDER(parameters -> {
        return storeProviderTemplatesFrom(parameters.find(MODEL_CLASSIFICATION))
                .get(parameters.find(STORAGE_TYPE));
    }, (name, parameters) -> {
        final StorageType storageType = parameters.find(STORAGE_TYPE);
        final ModelClassification modelClassification = parameters.find(MODEL_CLASSIFICATION);
        return storageType.resolveProviderNameFrom(modelClassification);
    });

    private static final String FILE_EXTENSION = ".java";

    private final Function<CodeTemplateParameters, String> templateFileRetriever;
    private final BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> parametersEnrichener;
    private final BiFunction<String, CodeTemplateParameters, String> nameResolver;

    CodeTemplateStandard(final Function<CodeTemplateParameters, String> templateFileRetriever) {
        this(templateFileRetriever, (name, parameters) -> name);
    }

    CodeTemplateStandard(final Function<CodeTemplateParameters, String> templateFileRetriever,
                         final BiFunction<String, CodeTemplateParameters, String> nameResolver) {
        this((parameters, file) -> parameters, templateFileRetriever, nameResolver);
    }

    CodeTemplateStandard(final BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> parametersEnrichener,
                         final Function<CodeTemplateParameters, String> templateFileRetriever) {
        this(parametersEnrichener, templateFileRetriever, (name, parameters) -> name);
    }

    CodeTemplateStandard(final BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> parametersEnrichener,
                         final Function<CodeTemplateParameters, String> templateFileRetriever,
                         final BiFunction<String, CodeTemplateParameters, String> nameResolver) {
        this.templateFileRetriever = templateFileRetriever;
        this.parametersEnrichener = parametersEnrichener;
        this.nameResolver = nameResolver;
    }

    public String retrieveTemplateFilename(final CodeTemplateParameters parameters) {
        return templateFileRetriever.apply(parameters);
    }

    public CodeTemplateParameters enrich(final CodeTemplateParameters parameters, final File file) {
        return this.parametersEnrichener.apply(parameters, file);
    }

    public String resolveClassname(final String name) {
        return this.nameResolver.apply(name, null);
    }

    public String resolveClassname(final CodeTemplateParameters parameters) {
        return this.nameResolver.apply(null, parameters);
    }

    public String resolveFilename(final String name, final CodeTemplateParameters parameters) {
        return this.nameResolver.apply(name, parameters) + FILE_EXTENSION;
    }

    public String resolveFilename(final CodeTemplateParameters parameters) {
        return this.nameResolver.apply(null, parameters) + FILE_EXTENSION;
    }

    private static BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> domainEventNameEnrichener() {
         return (parameters, file) -> {
            final String filename = FilenameUtils.removeExtension(file.getName());
            return parameters.and(DOMAIN_EVENT_NAME, filename) ;
        };
    }
}
