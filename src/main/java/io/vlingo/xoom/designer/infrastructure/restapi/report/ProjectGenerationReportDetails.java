// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.report;

import io.vlingo.xoom.designer.Configuration;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.designermodel.DesignerModelFormatter;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class ProjectGenerationReportDetails {

  private final static String DETAILS_PATTERN = "`Operation: Project Generation \n\n " +
                  "`Version`: %s \n\n " + "`Target`: %s \n\n " + "`Error Type`: %s \n\n " +
                  "`Designer Model`: \n ``` %s ``` \n\n " + "`Stacktrace`: \n ``` %s ```";

  public static String format(final String target,
                              final String errorType,
                              final GenerationSettingsData settingsData,
                              final Exception exception) {
    return format(target, errorType, DesignerModelFormatter.format(settingsData), exception);
  }

  public static String format(final String target,
                              final String errorType,
                              final String designerModel,
                              final Exception exception) {
    final String designerVersion = Configuration.resolveDefaultXoomVersion();
    final String stacktrace = Stream.of(exception.getStackTrace()).map(Object::toString).collect(joining("\n"));
    return String.format(DETAILS_PATTERN, designerVersion, target, errorType, designerModel, stacktrace);
  }

}
