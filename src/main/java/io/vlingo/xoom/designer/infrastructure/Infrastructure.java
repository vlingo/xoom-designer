// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.codegen.InvalidResourcesPathException;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class Infrastructure {

  public static void resolveInternalResources(final HomeDirectory homeDirectory) {
    if (!homeDirectory.isValid()) {
      throw new InvalidResourcesPathException();
    }
    StagingFolder.resolve(homeDirectory);
    DesignerProperties.resolve(homeDirectory);
    DesignerServer.resolve();
    UserInterface.resolve();
  }

  public static void resolveExternalResources(final ExternalDirectory externalDirectory) {
    XoomProperties.resolve(externalDirectory);
  }

  static Properties loadProperties(final Path path) {
    try {
      final File propertiesFile = path.toFile();
      final Properties properties = new Properties();
      if (propertiesFile.exists()) {
        properties.load(new FileInputStream(propertiesFile));
      }
      return properties;
    } catch (final IOException exception) {
      exception.printStackTrace();
      throw new ResourceLoadException(path);
    }
  }

  public static void clear() {
    ComponentRegistry.unregister(StagingFolder.class, DesignerProperties.class,
            DesignerServer.class, UserInterface.class, XoomProperties.class);
  }

}
