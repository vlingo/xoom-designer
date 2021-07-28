// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.task.projectgeneration.InvalidResourcesPathException;
import io.vlingo.xoom.turbo.ApplicationProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static io.vlingo.xoom.designer.task.Property.DESIGNER_SERVER_PORT;

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
    StagingFolder.instance = null;
    DesignerProperties.instance = null;
    DesignerServer.instance = null;
    UserInterface.instance = null;
    XoomProperties.instance = null;
  }

  public static class StagingFolder {
    private static final String ARCHETYPES_PARENT_FOLDER = "staging";
    private static StagingFolder instance;
    private final Path path;

    private static void resolve(final HomeDirectory homeDirectory) {
      if(instance == null){
        instance = new StagingFolder(homeDirectory);
      }
    }

    private StagingFolder(final HomeDirectory homeDirectory) {
      this.path = Paths.get(homeDirectory.path, ARCHETYPES_PARENT_FOLDER);
    }

    public static Path path() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Archetypes Folder");
      }
      return instance.path;
    }
  }

  public static class DesignerServer {
    private static DesignerServer instance;
    private static final int DEFAULT_SERVER_PORT = 19090;
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private final URL url;

    private static void resolve() {
      if(instance == null) {
        instance = new DesignerServer();
      }
    }

    private DesignerServer() {
      try {
        final int port = DesignerProperties.retrieveServerPort(DEFAULT_SERVER_PORT);
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
    private static final String USER_INTERFACE_CONTEXT = "context"; // "xoom-designer": This will not work until a resource for it is created.
    private final String rootContext;

    private static void resolve() {
      if(instance == null) {
        instance = new UserInterface();
      }
    }

    public UserInterface() {
      rootContext = String.format("%s/%s", DesignerServer.url(), USER_INTERFACE_CONTEXT);
    }

    public static String rootContext() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved User Interface");
      }
      return instance.rootContext;
    }
  }

  public static class DesignerProperties {
    private static final String FILENAME = "xoom-designer.properties";
    private static DesignerProperties instance;
    private final Properties properties;

    private static void resolve(final HomeDirectory homeDirectory) {
      if(instance == null) {
        instance = new DesignerProperties(homeDirectory);
      }
    }

    private DesignerProperties(final HomeDirectory homeDirectory) {
      this.properties = loadProperties(Paths.get(homeDirectory.path, FILENAME));
    }

    public static int retrieveServerPort(final int defaultPort) {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Designer Properties");
      }
      final String port =
              ApplicationProperty.readValue(DESIGNER_SERVER_PORT.literal(), instance.properties);

      return port == null ? defaultPort : Integer.valueOf(port);
    }

    public static Properties properties() {
      if(instance == null) {
        throw new IllegalStateException("Unresolved Designer Properties");
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
      final String subFolder = Profile.isTestProfileEnabled() ? "test" : "main";
      return Paths.get(externalDirectory.path, "src", subFolder, "resources", "xoom-turbo.properties");
    }
  }
}
