package io.vlingo.xoom.starter.task.template.code;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.starter.Configuration.*;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public enum CodeTemplateStandard {

    AGGREGATE(parameters -> AGGREGATE_TEMPLATES.get(parameters.from(STORAGE_TYPE))),
    STATE(parameters -> STATE_TEMPLATES.get(parameters.from(STORAGE_TYPE))),
    AGGREGATE_PROTOCOL(parameters -> CodeTemplateFile.AGGREGATE_PROTOCOL.filename),
    REST_RESOURCE(parameters -> CodeTemplateFile.REST_RESOURCE.filename),
    DOMAIN_EVENT(domainEventNameEnrichener(), parameters -> CodeTemplateFile.DOMAIN_EVENT.filename),
    PLACEHOLDER_DOMAIN_EVENT(domainEventNameEnrichener(), parameters -> CodeTemplateFile.PLACEHOLDER_DOMAIN_EVENT.filename),
    STATE_ADAPTER(parameters -> STATE_ADAPTER_TEMPLATES.get(parameters.from(STORAGE_TYPE))),
    STORE_PROVIDER_CONFIGURATION(parameters -> CodeTemplateFile.STORE_PROVIDER_CONFIGURATION.filename),
    STORE_PROVIDER(parameters -> {
        return storeProviderTemplatesFrom(parameters.from(MODEL_CLASSIFICATION)).get(parameters.from(STORAGE_TYPE));
    });

    private final Function<CodeTemplateParameters, String> templateFileRetriever;
    private final BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> parametersEnrichener;

    CodeTemplateStandard(final Function<CodeTemplateParameters, String> templateFileRetriever) {
        this((parameters, file) -> parameters, templateFileRetriever);
    }

    CodeTemplateStandard(final BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> parametersEnrichener,
                         final Function<CodeTemplateParameters, String> templateFileRetriever) {
        this.templateFileRetriever = templateFileRetriever;
        this.parametersEnrichener = parametersEnrichener;
    }

    public String retrieveTemplateFilename(final CodeTemplateParameters parameters) {
        return templateFileRetriever.apply(parameters);
    }

    public CodeTemplateParameters enrich(final CodeTemplateParameters parameters, final File file) {
        return this.parametersEnrichener.apply(parameters, file);
    }

    private static BiFunction<CodeTemplateParameters, File, CodeTemplateParameters> domainEventNameEnrichener() {
         return (parameters, file) -> {
            final String filename = FilenameUtils.removeExtension(file.getName());
            return parameters.and(DOMAIN_EVENT_NAME, filename) ;
        };
    }
}
