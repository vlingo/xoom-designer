package io.vlingo.xoom.designer.task.reactjs;

import freemarker.cache.FileTemplateLoader;
import freemarker.core.PlainTextOutputFormat;
import freemarker.template.*;
import io.vlingo.xoom.turbo.codegen.template.TemplateProcessorConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class ReactJSCodeGenerator {

    private final String templateRootPath = "/home/hurlee/IdeaProjects/vlingo-xoom/src/main/resources/codegen/reactjs/";
    //private final Configuration configuration = TemplateProcessorConfiguration.instance().configuration;
    private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);

    public ReactJSCodeGenerator() throws IOException {
        DefaultObjectWrapper objectWrapper = new DefaultObjectWrapper(Configuration.VERSION_2_3_30);
        objectWrapper.setExposeFields(true);
        configuration.setTemplateLoader(new FileTemplateLoader(new File(templateRootPath)));
        configuration.setObjectWrapper(objectWrapper);
        configuration.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try{
            configuration.setSharedVariable("fns", new TemplateCustomFunctions());
        }catch (TemplateException e){
            throw new RuntimeException("can't add fns functions", e);
        }
    }

    public void generateWith(String templatePath, Object arguments, Writer output) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath + ".ftl");
        template.process(arguments, output);
    }
}
