// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e;

import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.cli.CommandLineInterfaceInitializer;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.vlingo.xoom.http.Response.Status.Created;
import static io.vlingo.xoom.http.Response.Status.Ok;

public abstract class ProjectGenerationTest {

  private static Integer designerPort;
  private static final Logger logger = Logger.basicLogger();
  private static final PortDriver portDriver = PortDriver.init();
  private static final String apiRootPath = "/api/model-processing";
  public static final Path e2eResourcesPath = Paths.get(System.getProperty("user.dir"), "src", "e2e-test", "resources");

  public static void init() {
    onShutdown();
    if(!isDesignerRunning()) {
      designerPort = portDriver.findAvailable();
      CommandLineInterfaceInitializer.main(new String[]{"designer", "--port", designerPort.toString(), "--profile", "test"});
    }
  }

  public void generateAndRun(final Project project) {
    generate(project);
    compile(project);
    run(project);
  }

  public RequestSpecification apiOf(final Project project) {
    return given().port(project.appPort).accept(JSON).contentType(JSON);
  }

  private void generate(final Project project){
    removeTargetFolder(project.generationPath.path);

    final int pathCreationStatusCode = given().port(designerPort).accept(JSON)
            .contentType(JSON).body(project.generationPath).post(apiRootPath + "/paths").statusCode();

    Assertions.assertEquals(Created.code, pathCreationStatusCode, "Error creating generation path for " + project);

    final int generationStatusCode = given().port(designerPort)
            .accept(JSON).contentType(JSON).body(project.generationSettings).post(apiRootPath).statusCode();

    Assertions.assertEquals(Ok.code, generationStatusCode, "Error generating " + project);
  }

  protected abstract void compile(final Project project);

  protected abstract void run(final Project project);

  protected void assertCompilation(final ExecutionStatus status, final Project project) {
    Assertions.assertEquals(ExecutionStatus.SUCCEEDED, status, "Error compiling " + project);
  }

  protected void assertInitialization(final Project project) {
    Assertions.assertFalse(portDriver.isPortAvailable(project.appPort, 300, 30, false), "Error initializing app " + project);
  }

  protected void assertServiceIsAvailable(final int port, final String failureMessage) {
    Assertions.assertFalse(portDriver.isPortAvailable(port, 1000, 10, false), failureMessage);
  }

  private void removeTargetFolder(final String generationPath) {
    try {
      FileUtils.deleteDirectory(new File(generationPath));
    } catch (final IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to remove target folder", e);
    }
  }

  private static void onShutdown() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      stopServices();
      SupportingServicesManager.shutdown();
    }));
  }

  public static void stopServices() {
    Project.stopAll(logger, portDriver);
  }

  private static boolean isDesignerRunning() {
    return designerPort != null;
  }

}
