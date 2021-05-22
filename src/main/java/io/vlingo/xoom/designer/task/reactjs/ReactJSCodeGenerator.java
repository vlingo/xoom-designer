package io.vlingo.xoom.designer.task.reactjs;

import freemarker.template.*;
import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;
import io.vlingo.xoom.codegen.template.TemplateProcessorConfiguration;

import java.io.IOException;
import java.io.Writer;

public class ReactJSCodeGenerator {

    private final Configuration configuration = TemplateProcessorConfiguration.instance().configuration;

    public ReactJSCodeGenerator() throws IOException {
        // TODO move into xoom-turbo module
        try{
            configuration.setSharedVariable("fns", TemplateCustomFunctions.instance());
        }catch (TemplateException e){
            throw new RuntimeException("can't add fns functions", e);
        }
    }

    public void generateWith(String templatePath, Object arguments, Writer output) throws IOException, TemplateException {
        Template template = configuration.getTemplate("/codegen/reactjs/" + templatePath + ".ftl");
        template.process(arguments, output);
    }
}
