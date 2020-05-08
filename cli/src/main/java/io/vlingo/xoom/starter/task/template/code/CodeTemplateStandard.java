package io.vlingo.xoom.starter.task.template.code;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.starter.Configuration.AGGREGATE_TEMPLATES;
import static io.vlingo.xoom.starter.Configuration.STATE_TEMPLATES;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.DOMAIN_EVENT_NAME;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.STORAGE_TYPE;

public enum CodeTemplateStandard {

    STATE((parameters) -> {
        return STATE_TEMPLATES.get(parameters.from(STORAGE_TYPE));
    }),
    AGGREGATE((parameters) -> {
        return AGGREGATE_TEMPLATES.get(parameters.from(STORAGE_TYPE));
    }),
    AGGREGATE_PROTOCOL((parameters) -> CodeTemplateFile.AGGREGATE_PROTOCOL.filename),
    REST_RESOURCE((parameters) -> CodeTemplateFile.REST_RESOURCE.filename),
    STORE_PROVIDER((parameters) -> null),
    DOMAIN_EVENT(domainEventNameEnrichener(),
            (parameters) -> CodeTemplateFile.DOMAIN_EVENT.filename),
    PLACEHOLDER_DOMAIN_EVENT(domainEventNameEnrichener(),
            (parameters) -> CodeTemplateFile.PLACEHOLDER_DOMAIN_EVENT.filename);

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
