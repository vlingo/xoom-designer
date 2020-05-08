package io.vlingo.xoom.starter.task.template.code;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CodeTemplateProcessor {

    private static CodeTemplateProcessor INSTANCE;
    private static final String TEMPLATE_PATH_PATTERN = "codegen/%s.ftl";

    private final Configuration configuration;

    private CodeTemplateProcessor() {
        this.configuration = configure();
    }

    public static CodeTemplateProcessor instance(){
        if(INSTANCE == null) {
            INSTANCE = new CodeTemplateProcessor();
        }
        return INSTANCE;
    }

    public String process(final CodeTemplateStandard standard, final CodeTemplateParameters parameters) {
        try {
            final String templateFilename =
                    standard.retrieveTemplateFilename(parameters);

            final String templatePath =
                    String.format(TEMPLATE_PATH_PATTERN, templateFilename);

            final Template template =
                    configuration.getTemplate(templatePath);

            final Writer consoleWriter = new StringWriter();
            template.process(parameters.map(), consoleWriter);
            return consoleWriter.toString();
        } catch (final IOException | TemplateException exception) {
            throw new TemplateGenerationException(exception);
        }
    }

    private Configuration configure() {
        final Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(this.getClass(), "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocale(Locale.US);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

}
