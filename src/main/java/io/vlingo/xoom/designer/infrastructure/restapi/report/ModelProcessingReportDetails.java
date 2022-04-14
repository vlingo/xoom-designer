// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;

import java.io.IOException;

public class ModelProcessingReportDetails {

  private final static String DETAILS_PATTERN =
                  "**Action**: Project Generation <br> **OS**:  %s <br> **Java Version**:  %s <br>" +
                  "**Designer Version**: %s <br> **Target**: %s <br> **Error Type**: %s <br>" +
                  "**Designer Model**: <br><pre> %s </pre><br> " +
                   "**Stacktrace**: <br><pre>%s</pre>";

  public static String format(final String target,
                              final String errorType,
                              final DesignerModel settingsData,
                              final Exception exception) {
    return format(target, errorType, JsonSerialization.serialized(settingsData), exception);
  }

  public static String format(final String target,
                              final String errorType,
                              final String designerModel,
                              final Exception exception) {
    try {
      final String osName = System.getProperty("os.name");
      final String javaVersion = System.getProperty("java.version");
      final String stacktrace = ExceptionFormatter.format(exception);
      final String designerVersion = Configuration.resolveDefaultXoomVersion();
      final String simpleModelJson = designerModel.replaceAll("\n", "<br>").replaceAll("\\\\\"", "\"");
      return String.format(DETAILS_PATTERN, osName, javaVersion, designerVersion,
              target, errorType, simpleModelJson, stacktrace);
    } catch (final IOException e) {
      throw new ReportFormattingException(e);
    }
  }

}
