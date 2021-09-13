// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.terminal;

import io.vlingo.xoom.actors.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommandOutputConsumer {

  private final Logger logger;
  private final Process process;

  public static CommandOutputConsumer of(final Process process) {
    return of(Logger.basicLogger(), process);
  }

  public static CommandOutputConsumer of(final Logger logger, final Process process) {
    return new CommandOutputConsumer(logger, process);
  }

  private CommandOutputConsumer(Logger logger, Process process) {
    this.logger = logger;
    this.process = process;
  }

  public void tail() {
    consumeWith(logger::info);
  }

  public void consumeWith(final Consumer<String> consumer) {
    final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    final Stream<BufferedReader> processReaders =
            Stream.of(process.getInputStream(), process.getErrorStream())
                    .map(inputStream -> new BufferedReader(new InputStreamReader(inputStream)));

    final Consumer<BufferedReader> readerConsumer =
            reader -> executor.submit(() -> reader.lines().forEach(consumer));

    processReaders.forEach(readerConsumer);
  }

}
