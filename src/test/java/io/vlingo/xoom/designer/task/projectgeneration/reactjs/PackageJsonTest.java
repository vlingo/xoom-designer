package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ContextSettingsData;
import io.vlingo.xoom.designer.task.reactjs.HeaderArguments;
import io.vlingo.xoom.designer.task.reactjs.IndexPageArguments;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageJsonTest extends BaseTemplateTest {

    @Test
    public void test1() throws Exception{
        String result = generate("package.json", new IndexPageArguments(new ContextSettingsData(
            "io.vlingo.dev", "xoom-petclinic", "0.0.1", "io.vlingo.dev.petclinic"
        )));

        assertTrue(result.contains("\"name\": \"io.vlingo.dev.xoom-petclinic\","));
        assertTrue(result.contains("\"version\": \"0.0.1\","));
    }
}
