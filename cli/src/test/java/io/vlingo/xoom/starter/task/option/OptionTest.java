package io.vlingo.xoom.starter.task.option;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testValueFoundConditionOnNonRequiredOption() {
        final Option option = Option.of(OptionName.TAG, "latest");
        final List<String> args = Arrays.asList("docker", "package", "--tag", "0.0.1", "--user", "danilo");
        Assert.assertEquals("0.0.1", option.findValue(args));
    }

    @Test
    public void testValueNotFoundConditionOnNonRequiredOption() {
        final Option option = Option.of(OptionName.TAG, "latest");
        final List<String> args = Arrays.asList("docker", "package", "--currentDirectory", "/home/users/projects/starter", "--user", "danilo");
        Assert.assertEquals("latest", option.findValue(args));
    }

    @Test
    public void testValueFoundConditionOnRequiredOption() {
        final Option option = Option.required(OptionName.CURRENT_DIRECTORY);
        final List<String> args = Arrays.asList("docker", "package", "--currentDirectory", "/home/users/projects/starter", "--user", "danilo");
        Assert.assertEquals("/home/users/projects/starter", option.findValue(args));
    }

    @Test(expected = RequiredOptionNotFoundException.class)
    public void testValueNotFoundConditionOnRequiredOption() {
        final Option option = Option.required(OptionName.CURRENT_DIRECTORY);
        final List<String> args = Arrays.asList("docker", "package", "--tag", "1.0.1", "--user", "danilo");
        option.findValue(args);
    }

}
