package io.vlingo.xoom.designer.task.projectgeneration.reactjs;

import io.vlingo.xoom.designer.task.reactjs.TemplateCustomFunctions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateCustomFunctionsTest {

    private TemplateCustomFunctions fns = new TemplateCustomFunctions();

    @Test
    public void capitalizeTest() {
        assertEquals("Customer", fns.capitalize("customer"));
        assertEquals("Customer", fns.capitalize("Customer"));
    }

    @Test
    public void decapitalizeTest() {
        assertEquals("customer", fns.decapitalize("customer"));
        assertEquals("customer", fns.decapitalize("Customer"));
    }

    @Test
    public void capitalizeMultiWordTest() {
        assertEquals("Xoom Petclinic", fns.capitalizeMultiWord("xoom petclinic"));
    }

    @Test
    public void makePluralTest() {
        assertEquals("Pets", fns.makePlural("Pet"));
        assertEquals("Houses", fns.makePlural("House"));
        assertEquals("Buses", fns.makePlural("Bus"));
        assertEquals("Lunches", fns.makePlural("Lunch"));
        assertEquals("Cities", fns.makePlural("City"));
        assertEquals("Puppies", fns.makePlural("Puppy"));
        assertEquals("Boys", fns.makePlural("Boy"));
    }
}
