package io.vlingo.xoom.starter.task.template.code;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.template.TemplateGenerationException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CodeTemplateProcessor {

    private static CodeTemplateProcessor INSTANCE;
    private static final String TEMPLATE_PATH_PATTERN = "codegen/%s.ftl";

    private CodeTemplateProcessor() {
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
                    Configuration.freeMarkerSettings().getTemplate(templatePath);

            final Writer writer = new StringWriter();
            template.process(parameters.map(), writer);
            return writer.toString();
        } catch (final IOException | TemplateException exception) {
            throw new TemplateGenerationException(exception);
        }
    }

}
