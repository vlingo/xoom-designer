// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationPath;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Project {

  public final int appPort;
  public final String directory;
  public final String modelFilename;
  public final GenerationPath generationPath;
  public final GenerationSettingsData generationSettings;
  private static List<Project> all =  new ArrayList<>();

  public static Project from(final String directory,
                             final String modelFilename) {
    final int availablePort = PortDriver.init().findAvailable();
    return new Project(directory, modelFilename, availablePort);
  }

  private Project(final String directory, final String modelFilename, int availablePort) {
    this.directory = directory;
    this.modelFilename = modelFilename;
    this.appPort = availablePort;
    this.generationPath = buildGenerationPath();
    this.generationSettings = buildGenerationSettings();
    all.add(this);
  }

  private GenerationSettingsData buildGenerationSettings() {
    final String modelPath =
            String.format("/sample-models/%s/%s.json", directory, modelFilename);

    try {
      final String generationPath = resolveGenerationPath(modelFilename);
      final InputStream modelStream = ProjectGenerationTest.class.getResourceAsStream(modelPath);
      final String modelJson = IOUtils.toString(modelStream, StandardCharsets.UTF_8.name());
      return JsonSerialization.deserialized(String.format(modelJson, generationPath), GenerationSettingsData.class);
    } catch (final IOException exception) {
      throw new RuntimeException(String.format("Failed to load Designer model from `%s`.", modelPath), exception);
    }
  }

  private GenerationPath buildGenerationPath() {
    return new GenerationPath(resolveGenerationPath(modelFilename));
  }

  private String resolveGenerationPath(final String model) {
    return Paths.get(System.getProperty("user.dir"), "target", "e2e-tests", model)
            .toString().replace("\\", "\\\\");
  }

  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("directory", directory)
            .append("modelFileName", modelFilename)
            .append("appPort", appPort)
            .toString();
  }

  public static void stopAll(final Logger logger, final PortDriver portDriver) {
    all.forEach(project -> {
      if(!portDriver.release(project.appPort)) {
        logger.warn("Unable to release port " + project.appPort);
      } else {
        logger.info("Port " + project.appPort + " released");
      }
    });
    all.clear();
  }

}
