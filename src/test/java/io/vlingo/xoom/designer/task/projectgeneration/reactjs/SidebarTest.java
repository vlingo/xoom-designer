package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.APIData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.AggregateData;
import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ContextSettingsData;
import io.vlingo.xoom.designer.task.reactjs.AppArguments;
import io.vlingo.xoom.designer.task.reactjs.HeaderArguments;
import io.vlingo.xoom.designer.task.reactjs.SidebarArguments;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SidebarTest extends BaseTemplateTest {

    @Test
    public void test1() throws Exception{
        String result = generate("Sidebar", new SidebarArguments(Arrays.asList(
                new AggregateData(
                        "Pet",
                        new APIData("/pets", asList()),
                        asList(),
                        asList(),
                        asList(),
                        null,
                        null
                ),
                new AggregateData(
                        "Customer",
                        new APIData("/customers", asList()),
                        asList(),
                        asList(),
                        asList(),
                        null,
                        null
                )
        )));

        assertTrue(result.contains("<MenuItem label={'Pet'} link={'/app/pets'}/>"));
        assertTrue(result.contains("<MenuItem label={'Customer'} link={'/app/customers'}/>"));
    }
}
