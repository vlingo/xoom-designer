// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import io.vlingo.xoom.starter.task.TaskExecutionStep;
import io.vlingo.xoom.starter.task.TaskExecutionContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class GenerationLogStep implements TaskExecutionStep {

    @Override
    public void process(final TaskExecutionContext context) {
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
