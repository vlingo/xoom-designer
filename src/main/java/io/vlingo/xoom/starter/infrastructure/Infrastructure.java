// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.infrastructure;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.projectgeneration.InvalidResourcesPathException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.starter.task.Property.STARTER_SERVER_PORT;

public class Infrastructure {

  public static void resolveInternalResources(final HomeDirectory homeDirectory) {
    if (!homeDirectory.isValid()) {
      throw new InvalidResourcesPathException();
    }
    ArchetypesFolder.resolve(homeDirectory);
    StarterProperties.resolve(homeDirectory);
    StarterServer.resolve();
    UserInterface.resolve();
  }

  public static void resolveExternalResources(final ExternalDirectory externalDirectory) {
    XoomProperties.resolve(externalDirectory);
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

  public static void clear() {
    ArchetypesFolder.instance = null;
    StarterProperties.instance = null;
    StarterServer.instance = null;
    UserInterface.instance = null;
    XoomProperties.instance = null;
  }

  public static class ArchetypesFolder {
    private static final String ARCHETYPES_SUB_FOLDER = "archetypes";
    private static final String ARCHETYPES_PARENT_FOLDER = "resources";
    private static ArchetypesFolder instance;
    private final Path path;

    private static void resolve(final HomeDirectory homeDirectory) {
      if(instance == null){
        instance = new ArchetypesFolder(homeDirectory);
      }
    }

    private ArchetypesFolder(final HomeDirectory homeDirectory) {
      this.path = Paths.get(homeDirectory.path, ARCHETYPES_PARENT_FOLDER, ARCHETYPES_SUB_FOLDER);
    }
    public static Path path() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Archetypes Folder");
      }
      return instance.path;
    }
  }

  public static class StarterServer {
    private static StarterServer instance;
    private static final int DEFAULT_SERVER_PORT = 19090;
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private final URL url;

    private static void resolve() {
      if(instance == null) {
        instance = new StarterServer();
      }
    }

    private StarterServer() {
      try {
        final int port = StarterProperties.retrieveServerPort(DEFAULT_SERVER_PORT);
        this.url = new URL(String.format("http://%s:%s", DEFAULT_SERVER_HOST, port));
      } catch (final MalformedURLException e) {
        throw new IllegalStateException(e);
      }
    }

    public static URL url() {
      return instance.url;
    }
  }

  public static class UserInterface {
    private static UserInterface instance;
    private static final String USER_INTERFACE_CONTEXT = "xoom-starter";
    private final String rootContext;

    private static void resolve() {
      if(instance == null) {
        instance = new UserInterface();
      }
    }

    public UserInterface() {
      rootContext = String.format("%s/%s", StarterServer.url(), USER_INTERFACE_CONTEXT);
    }

    public static String rootContext() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved User Interface");
      }
      return instance.rootContext;
    }
  }

  public static class StarterProperties {
    private static final String FILENAME = "vlingo-xoom-starter.properties";
    private static StarterProperties instance;
    private final Properties properties;

    private static void resolve(final HomeDirectory homeDirectory) {
      if(instance == null) {
        instance = new StarterProperties(homeDirectory);
      }
    }

    private StarterProperties(final HomeDirectory homeDirectory) {
      this.properties = loadProperties(Paths.get(homeDirectory.path, FILENAME));
    }

    public static int retrieveServerPort(final int defaultPort) {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Starter Properties");
      }
      final Object port = instance.properties.getOrDefault(STARTER_SERVER_PORT.literal(), defaultPort);
      return Integer.valueOf(port.toString());
    }

    public static Properties properties() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Starter Properties");
      }
      return instance.properties;
    }
  }

  public static class XoomProperties {
    private final Properties properties;
    private static XoomProperties instance;

    private static void resolve(final ExternalDirectory externalDirectory) {
      if(instance == null) {
        instance = new XoomProperties(externalDirectory);
      }
    }

    private XoomProperties(final ExternalDirectory externalDirectory) {
      this.properties = loadProperties(buildPath(externalDirectory));
    }

    public static Properties properties() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Xoom Properties");
      }
      return instance.properties;
    }

    private Path buildPath(final ExternalDirectory externalDirectory) {
      final String subFolder = Configuration.isTestProfileEnabled() ? "test" : "main";
      return Paths.get(externalDirectory.path, "src", subFolder, "resources", "vlingo-xoom.properties");
    }
  }
}
