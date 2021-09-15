// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.cli;

import io.vlingo.xoom.designer.infrastructure.ExternalDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.XoomProperties;
import io.vlingo.xoom.designer.infrastructure.ResourceLoadException;

import java.util.Properties;

import static io.vlingo.xoom.designer.cli.OptionName.CURRENT_DIRECTORY;

public class XoomPropertiesLoadStep implements TaskExecutionStep {

  @Override
  public void process(final TaskExecutionContext context) {
      final ExternalDirectory externalDirectory =
              ExternalDirectory.from(context.optionValueOf(CURRENT_DIRECTORY));
      context.onProperties(loadProperties(externalDirectory));
  }

  private Properties loadProperties(final ExternalDirectory externalDirectory) {
    Infrastructure.resolveExternalResources(externalDirectory);
    if(XoomProperties.properties().isEmpty()) {
      throw new ResourceLoadException("Unable to read xoom-turbo.properties");
    }
    return XoomProperties.properties();
  }



}
