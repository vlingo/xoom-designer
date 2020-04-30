package io.vlingo.xoom.starter.task.option;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testValueFoundConditionOnNonRequiredOption() {
        final Option option = Option.of(OptionName.TAG, "latest");
        final List<String> args = Arrays.asList("docker", "package", "--tag", "0.0.1", "--user", "danilo");
        Assertions.assertEquals("0.0.1", option.findValue(args));
    }

    @Test
    public void testValueNotFoundConditionOnNonRequiredOption() {
        final Option option = Option.of(OptionName.TAG, "latest");
        final List<String> args = Arrays.asList("docker", "package", "--currentDirectory", "/home/users/projects/starter", "--user", "danilo");
        Assertions.assertEquals("latest", option.findValue(args));
    }

    @Test
    public void testValueFoundConditionOnRequiredOption() {
        final Option option = Option.required(OptionName.CURRENT_DIRECTORY);
        final List<String> args = Arrays.asList("docker", "package", "--currentDirectory", "/home/users/projects/starter", "--user", "danilo");
        Assertions.assertEquals("/home/users/projects/starter", option.findValue(args));
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
