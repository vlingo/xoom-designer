package io.vlingo.xoomstarter.template.steps;

import io.vlingo.xoomstarter.template.TemplateGenerationContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class GenerationLogStep implements TemplateGenerationStep  {

    @Override
    public void process(final TemplateGenerationContext context) {
        log(context.process());
    }

    private void log(final Process process) {
        Arrays.asList(process.getInputStream(), process.getErrorStream())
                .stream().forEach(inputStream -> consume(inputStream));
    }

    private void consume(final InputStream inputStream) {
        Executors.newSingleThreadExecutor()
                .submit(() -> {
                    final BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    bufferedReader.lines().forEach(System.out::println);
                });
    }
}
