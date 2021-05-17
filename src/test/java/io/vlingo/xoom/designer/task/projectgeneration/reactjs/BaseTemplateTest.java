package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import freemarker.template.TemplateException;
import io.vlingo.xoom.designer.task.reactjs.ReactJSCodeGenerator;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Pattern;

public abstract class BaseTemplateTest {

    protected ReactJSCodeGenerator codeGenerator;

    @BeforeEach
    public void init() throws IOException {
        codeGenerator = new ReactJSCodeGenerator();
    }

    protected String generate(String templateName, Object arguments) throws TemplateException, IOException {
        StringWriter output = new StringWriter();
        codeGenerator.generateWith(templateName, arguments, output);
        return output.toString();
    }

    protected boolean containsPattern(Pattern pattern, String text) {
        return pattern.matcher(text).find();
    }
}
