// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.designer.Configuration;

import java.io.IOException;

public class ModelFileHandlingReport {

  private static final String DESIGNER_MODEL_EXPORT = "DESIGNER_MODEL_EXPORT";
  private static final String DESIGNER_MODEL_IMPORT = "DESIGNER_MODEL_IMPORT";
  private static final String DETAILS_PATTERN = "**Action**: %s <br> **OS**:  %s <br> **Java Version:** %s <br>" +
                            "**Designer Version**: %s <br>  **Stacktrace**: <br><pre>%s</pre>";

  public final String details;

  public static ModelFileHandlingReport onExportFail(final Exception exception) {
    return new ModelFileHandlingReport(DESIGNER_MODEL_EXPORT, exception);
  }

  public static ModelFileHandlingReport onImportFail(final Exception exception) {
    return new ModelFileHandlingReport(DESIGNER_MODEL_IMPORT, exception);
  }

  private ModelFileHandlingReport(final String action,
                                  final Exception exception) {
    try {
      final String osName = System.getProperty("os.name");
      final String javaVersion = System.getProperty("java.version");
      final String designerVersion = Configuration.resolveDefaultXoomVersion();
      this.details = String.format(DETAILS_PATTERN, action, osName, javaVersion,
              designerVersion, ExceptionFormatter.format(exception));
    } catch (final IOException e) {
      throw new ReportFormattingException(e);
    }
  }

}
