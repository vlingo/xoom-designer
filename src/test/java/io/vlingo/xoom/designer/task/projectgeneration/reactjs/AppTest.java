package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.*;
import io.vlingo.xoom.designer.task.reactjs.AppArguments;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest extends BaseTemplateTest {

    @Test
    public void test1() throws Exception{
        String result = generate("App", new AppArguments(Arrays.asList(
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

        assertTrue(result.contains("import Pets from \"./components/pets/Pets\";"));
        assertTrue(result.contains("import Pet from \"./components/pets/Pet\";"));
        assertTrue(result.contains("import Customers from \"./components/customers/Customers\";"));
        assertTrue(result.contains("import Customer from \"./components/customers/Customer\";"));
        assertTrue(result.contains("<Route path=\"/app/pets\" exact={true}><Pets /></Route>"));
        assertTrue(result.contains("<Route path=\"/app/pets/:id\"><Pet /></Route>"));
        assertTrue(result.contains("<Route path=\"/app/customers\" exact={true}><Customers /></Route>"));
        assertTrue(result.contains("<Route path=\"/app/customers/:id\"><Customer /></Route>"));
    }
}
