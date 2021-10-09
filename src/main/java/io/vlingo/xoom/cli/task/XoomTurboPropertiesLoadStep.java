// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.designer.infrastructure.ApplicationDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.ResourceLoadException;
import io.vlingo.xoom.designer.infrastructure.XoomTurboProperties;

import java.util.Properties;

import static io.vlingo.xoom.cli.option.OptionName.CURRENT_DIRECTORY;

public class XoomTurboPropertiesLoadStep implements TaskExecutionStep {

  @Override
  public void processTaskWith(final TaskExecutionContext context) {
      final ApplicationDirectory applicationDirectory =
              ApplicationDirectory.from(context.optionValueOf(CURRENT_DIRECTORY));
      context.onProperties(loadProperties(applicationDirectory));
  }

  private Properties loadProperties(final ApplicationDirectory applicationDirectory) {
    Infrastructure.resolveExternalResources(applicationDirectory);
    if(XoomTurboProperties.properties().isEmpty()) {
      throw new ResourceLoadException("Unable to read xoom-turbo.properties");
    }
    return XoomTurboProperties.properties();
  }

}
