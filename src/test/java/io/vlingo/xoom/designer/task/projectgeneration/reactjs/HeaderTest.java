package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ContextSettingsData;
import io.vlingo.xoom.designer.task.reactjs.HeaderArguments;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeaderTest extends BaseTemplateTest {

    @Test
    public void test1() throws Exception{
        String result = generate("Header", new HeaderArguments(new ContextSettingsData(
            "io.vlingo.dev", "xoom-petclinic", "0.0.1", "io.vlingo.dev.petclinic"
        )));

        assertTrue(containsPattern(Pattern.compile("<a .+ href=\"/\">Xoom Petclinic</a>"), result));
    }
}
