// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.cli;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.ResourceLoadException;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class XoomTurboProperties {

  public static final String DOCKER_IMAGE = "docker.image";
  public static final String GLOO_UPSTREAM = "gloo.upstream";
  public static final String DOCKER_REPOSITORY = "docker.repository";

  private final Properties properties;

  public static XoomTurboProperties load(final ProjectPath projectPath) {
    if (!ComponentRegistry.has(XoomTurboProperties.class)) {
      final Path propertiesPath = buildPath(projectPath);
      final Properties properties = loadProperties(propertiesPath);
      ComponentRegistry.register(XoomTurboProperties.class, new XoomTurboProperties(properties));
    }
    return ComponentRegistry.withType(XoomTurboProperties.class);
  }

  private XoomTurboProperties(final Properties properties) {
    this.properties = properties;
  }

  public String get(final String key) {
    return properties.getProperty(key);
  }

  public Stream<Entry<Object, Object>> filterProperties(final Predicate<Entry<Object, Object>> condition) {
    return properties.entrySet().stream().filter(condition);
  }

  public boolean hasProperty(final String key) {
    return properties.containsKey(key);
  }

  public static XoomTurboProperties empty() {
    return new XoomTurboProperties(new Properties());
  }

  private static Path buildPath(final ProjectPath projectPath) {
    final String subFolder = Profile.isTestProfileEnabled() ? "test" : "main";
    return Paths.get(projectPath.path, "src", subFolder, "resources", "xoom-turbo.properties");
  }

  private static Properties loadProperties(final Path path) {
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

  public static class ProjectPath {

    public final String path;

    public static ProjectPath from(final String path) {
      return new ProjectPath(path);
    }

    public ProjectPath(final String path) {
      this.path = path;
    }
  }
}
