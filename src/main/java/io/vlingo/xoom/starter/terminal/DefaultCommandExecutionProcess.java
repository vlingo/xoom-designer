// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.terminal;

import io.vlingo.xoom.starter.task.TaskExecutionException;
import io.vlingo.xoom.starter.task.projectgeneration.ProjectGenerationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DefaultCommandExecutionProcess extends CommandExecutionProcess {

  private static final String SUCCESS_MESSAGE = "Done!";
  private static final String FAILURE_MESSAGE = "Failed.";

  @Override
  protected Process execute(final String[] commands) {
    try {
      return Runtime.getRuntime().exec(commands);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new TaskExecutionException(e);
    }
  }

  @Override
  protected void log(final Process process) {
    final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    final Stream<BufferedReader> processReaders =
            Stream.of(process.getInputStream(), process.getErrorStream())
                    .map(inputStream -> new BufferedReader(new InputStreamReader(inputStream)));

    final Consumer<BufferedReader> readerConsumer =
            reader -> executor.submit(() -> reader.lines().forEach(System.out::println));

    processReaders.forEach(readerConsumer);
  }

  @Override
  protected void handleCommandExecutionStatus(final int commandExecutionStatus) {
    if(commandExecutionStatus == 0) {
      System.out.println(SUCCESS_MESSAGE);
    } else {
      throw new ProjectGenerationException(FAILURE_MESSAGE);
    }
  }
}
