package io.vlingo.xoom.designer.task.option;


import io.vlingo.xoom.designer.task.Option;
import io.vlingo.xoom.designer.task.OptionName;
import io.vlingo.xoom.designer.task.RequiredOptionNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testValueFoundConditionOnRequiredOption() {
        final Option option = Option.required(OptionName.CURRENT_DIRECTORY);
        final List<String> args = Arrays.asList("docker", "package", "--currentDirectory", "/home/users/projects/designer", "--user", "danilo");
        Assertions.assertEquals("/home/users/projects/designer", option.findValue(args));
    }

    @Test
    public void testValueNotFoundConditionOnRequiredOption() {
        final Option option = Option.required(OptionName.CURRENT_DIRECTORY);
        final List<String> args = Arrays.asList("docker", "package", "--tag", "1.0.1", "--user", "danilo");

        Assertions.assertThrows(RequiredOptionNotFoundException.class, () -> {
            option.findValue(args);
        });
    }

}
