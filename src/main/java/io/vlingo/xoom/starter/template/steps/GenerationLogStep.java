// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.template.steps;

import io.vlingo.xoom.starter.template.TemplateGenerationContext;

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
