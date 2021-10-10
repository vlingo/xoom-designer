// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli.task;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.ResourceLoadException;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class XoomTurboProperties {

  public static final String DOCKER_IMAGE = "docker.image";
  public static final String GLOO_UPSTREAM = "gloo.upstream";
  public static final String DOCKER_REPOSITORY = "docker.repository";

  private final Properties properties;

  static XoomTurboProperties resolve(final ApplicationPath applicationPath) {
    if (!ComponentRegistry.has(XoomTurboProperties.class)) {
      ComponentRegistry.register(XoomTurboProperties.class, new XoomTurboProperties(applicationPath));
    }
    return ComponentRegistry.withType(XoomTurboProperties.class);
  }

  private XoomTurboProperties(final ApplicationPath applicationPath) {
    this.properties = loadProperties(buildPath(applicationPath));
  }

  private Properties loadProperties(final Path path) {
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

  public Properties properties() {
    if (!ComponentRegistry.has(XoomTurboProperties.class)) {
      throw new IllegalStateException("Xoom Properties are not available.");
    }
    return ComponentRegistry.withType(XoomTurboProperties.class).properties;
  }

  public String propertyOf(final String key) {
    return properties.getProperty(key);
  }

  private Path buildPath(final ApplicationPath applicationPath) {
    final String subFolder = Profile.isTestProfileEnabled() ? "test" : "main";
    return Paths.get(applicationPath.path, "src", subFolder, "resources", "xoom-turbo.properties");
  }

  public static class ApplicationPath {

    public final String path;

    public static ApplicationPath from(final String path) {
      return new ApplicationPath(path);
    }

    public ApplicationPath(final String path) {
      this.path = path;
    }

  }
}
