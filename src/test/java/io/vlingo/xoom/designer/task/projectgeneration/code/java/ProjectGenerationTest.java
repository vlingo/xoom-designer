// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.restassured.http.ContentType;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.Profile;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import io.vlingo.xoom.designer.infrastructure.userinterface.UserInterfaceBootstrapStep;
import io.vlingo.xoom.designer.infrastructure.userinterface.XoomInitializer;
import io.vlingo.xoom.designer.task.TaskExecutionContext;
import io.vlingo.xoom.designer.task.projectgeneration.GenerationTarget;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.vlingo.xoom.designer.task.Property.DESIGNER_SERVER_PORT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaCompilationCommand.at;

public abstract class ProjectGenerationTest {

  private static final Logger logger = Logger.basicLogger();
  private static final Set<Integer> portsInUse = new HashSet<>();
  private static final PortDriver portDriver = PortDriver.init();

  public static void init() {
    Infrastructure.clear();
    ComponentRegistry.clear();
    Profile.enableTestProfile();
    ComponentRegistry.register(GenerationTarget.class, GenerationTarget.FILESYSTEM);
    ComponentRegistry.register(DESIGNER_SERVER_PORT.literal(), portDriver.findAvailable());
    ComponentRegistry.register("defaultCodeFormatter", CodeElementFormatter.with(Dialect.findDefault(), ReservedWordsHandler.usingSuffix("_")));
    Infrastructure.resolveInternalResources(HomeDirectory.fromEnvironment());
    new UserInterfaceBootstrapStep().process(TaskExecutionContext.bare());
    releasePortsOnShutdown();
  }

  public void generate(final String modelFilename){
    removeTargetFolder(modelFilename);

    final int designerPort = Infrastructure.DesignerServer.url().getPort();

    final int pathCreationStatusCode = given().port(designerPort).accept(ContentType.JSON)
            .contentType(ContentType.JSON).body(buildGenerationPath(modelFilename))
            .post("/api/generation-settings/paths").statusCode();

    Assertions.assertEquals(201, pathCreationStatusCode, "Error creating path for " + modelDirectory() + "/" + modelFilename);

    final int generationStatusCode = given().port(Infrastructure.DesignerServer.url().getPort())
            .accept(ContentType.JSON).contentType(ContentType.JSON).body(buildGenerationSettings(modelFilename))
            .post("/api/generation-settings").statusCode();

    Assertions.assertEquals(200, generationStatusCode, "Error generating " + modelDirectory() + "/" + modelFilename);
  }

  public void compile(final String modelFilename) {
    final String applicationPath = resolveGenerationPath(modelFilename);
    final JavaCompilationCommand compilationCommand = at(applicationPath);
    compilationCommand.process();
    Assertions.assertEquals(CommandStatus.SUCCEEDED, compilationCommand.status(), "Error compiling " + modelDirectory() + "/" + modelFilename);
  }

  public int run(final String modelFilename) {
    final int appPort = portDriver.findAvailable();

    portsInUse.add(appPort);

    final JavaAppInitializationCommand initializationCommand =
            JavaAppInitializationCommand.from(buildGenerationSettings(modelFilename), appPort);

    initializationCommand.process();

    Assertions.assertEquals(false, portDriver.isPortAvailable(appPort, 300, 30, false), "Error initializing app " + modelDirectory() + "/" + modelFilename);

    return appPort;
  }

  private void removeTargetFolder(final String model) {
    try {
      final String generationPath = resolveGenerationPath(model);
      FileUtils.deleteDirectory(new File(generationPath));
    } catch (final IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to remove target folder", e);
    }
  }

  private GenerationSettingsData buildGenerationSettings(final String modelFilename) {
    final String modelPath =
            String.format("/sample-models/%s/%s.json", modelDirectory(), modelFilename);

    try {
      final String generationPath = resolveGenerationPath(modelFilename);
      final InputStream modelStream = ProjectGenerationTest.class.getResourceAsStream(modelPath);
      final String modelJson = IOUtils.toString(modelStream, StandardCharsets.UTF_8.name());
      return JsonSerialization.deserialized(String.format(modelJson, generationPath), GenerationSettingsData.class);
    } catch (final IOException exception) {
      throw new RuntimeException(String.format("Failed to load Designer model from `%s`.", modelPath), exception);
    }
  }

  private GenerationPath buildGenerationPath(final String modelFilename) {
    return new GenerationPath(resolveGenerationPath(modelFilename));
  }

  public abstract String modelDirectory();

  public static void clear() throws Exception {
    Infrastructure.clear();
    ComponentRegistry.clear();
    Profile.disableTestProfile();
    XoomInitializer.instance().stopServer();
  }

  public static void releasePortsOnShutdown() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      portsInUse.forEach(port -> {
        if(!portDriver.release(port)) {
          logger.error("Unable to release port " + port);
        } else {
          logger.info("Port " + port + " released");
        }
      });
      portsInUse.clear();
    }));
  }

  private String resolveGenerationPath(final String model) {
    return Paths.get(System.getProperty("user.dir"), "target", "e2e-tests", model)
            .toString().replace("\\", "\\\\");
  }

}
