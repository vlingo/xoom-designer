// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Assertions;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Project {

  public final int appPort;
  public final String directory;
  public final String modelFilename;
  public final GenerationPath generationPath;
  public final GenerationSettingsData generationSettings;
  private boolean stopped;

  private static List<Project> all =  new ArrayList<>();

  public static Project from(final String directory,
                             final String modelFilename) {
    return new Project(directory, modelFilename, PortDriver.init());
  }

  private Project(final String directory, final String modelFilename, final PortDriver portDriver) {
    this.directory = directory;
    this.modelFilename = modelFilename;
    this.appPort = portDriver.findAvailable();
    this.generationPath = buildGenerationPath();
    this.generationSettings = buildGenerationSettings();
    all.add(this);
  }

  private GenerationSettingsData buildGenerationSettings() {
    final String completeModel = completeDynamicModelProperties();
    return JsonSerialization.deserialized(completeModel, GenerationSettingsData.class);
  }

  private String completeDynamicModelProperties() {
    final Path modelPath = resolveModelPath();
    return Assertions.assertDoesNotThrow(() -> {
      final String modelJson = FileUtils.readFileToString(modelPath.toFile(), StandardCharsets.UTF_8);
      return String.format(modelJson, appPort, resolveGenerationPath());
    }, String.format("Failed to load Designer model from `%s`.", modelPath));
  }

  private GenerationPath buildGenerationPath() {
    return new GenerationPath(resolveGenerationPath());
  }

  private String resolveGenerationPath() {
    return Paths.get(System.getProperty("user.dir"), "target", "e2e-tests", modelFilename)
            .toString().replace("\\", "\\\\");
  }

  private Path resolveModelPath() {
    return ProjectGenerationTest.e2eResourcesPath.resolve("sample-models").resolve(directory).resolve(modelFilename + ".json");
  }

  public static void stopAll(final Logger logger, final PortDriver portDriver) {
    all.forEach(project -> project.stop(logger, portDriver));
  }

  private void stop(final Logger logger, final PortDriver portDriver) {
    if(!stopped) {
      if (!portDriver.release(appPort)) {
        logger.warn("Unable to release port " + appPort);
      } else {
        this.stopped = true;
        logger.info("Port " + appPort + " released");
      }
    }
  }

  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("directory", directory)
            .append("modelFileName", modelFilename)
            .append("appPort", appPort)
            .toString();
  }

}
