// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.report;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ModelFileHandlingReport {

  private static final String DESIGNER_MODEL_EXPORT = "DESIGNER_MODEL_EXPORT";
  private static final String DESIGNER_MODEL_IMPORT = "DESIGNER_MODEL_IMPORT";
  private static final String DETAILS_PATTERN = "`Operation: %s \n\n" + "`Stacktrace`: \n ``` %s ```";

  public final String details;

  public static ModelFileHandlingReport onExportFail(final Exception exception) {
    return new ModelFileHandlingReport(DESIGNER_MODEL_EXPORT, exception);
  }

  public static ModelFileHandlingReport onImportFail(final Exception exception) {
    return new ModelFileHandlingReport(DESIGNER_MODEL_IMPORT, exception);
  }

  private ModelFileHandlingReport(final String operation,
                                  final Exception exception) {
    final String stacktrace = Stream.of(exception.getStackTrace()).map(Object::toString).collect(joining("\n"));
    this.details = String.format(DETAILS_PATTERN, operation, stacktrace);
  }

}
