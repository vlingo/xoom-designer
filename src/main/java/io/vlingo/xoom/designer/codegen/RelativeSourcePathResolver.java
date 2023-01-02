// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.template.TemplateData;
import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.*;

public interface RelativeSourcePathResolver {

  static String[] resolveWith(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
    final RelativeSourcePathResolver resolver =
            Stream.of(new ResourceFile(), new SchemataSpecification(), new PomFile(), new ReadmeFile(),
                    new KubernetesManifestFile(), new Dockerfile(), new DockerComposeFile(), new ProjectGenerationSettingsPayload(),
                    new SourceCode(), new UnitTest())
                    .filter(candidate -> candidate.shouldResolve(templateData))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to resolve relative source path"));

    return resolver.resolve(context, dialect, templateData);
  }

  String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData);

  boolean shouldResolve(final TemplateData templateData);

  class ResourceFile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{"src", "main", "resources"};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(RESOURCE_FILE, false);
    }
  }

  class SchemataSpecification implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{"src", "main", "vlingo", "schemata"};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(SCHEMATA_FILE, false);
    }
  }

  class PomFile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(POM_FILE, false) ||
              templateData.parameters().find(POM_SECTION, false);
    }
  }

  class Dockerfile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(DOCKERFILE, false);
    }
  }

  class DockerComposeFile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(DOCKER_COMPOSE_FILE, false);
    }
  }

  class ReadmeFile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(README_FILE, false);
    }
  }

  class KubernetesManifestFile implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{"deployment"};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return !templateData.parameters().find(KUBERNETES_IMAGE, "").isEmpty();
    }
  }

  class ProjectGenerationSettingsPayload implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      return new String[]{};
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(DESIGNER_MODEL, false);
    }
  }

  class SourceCode implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      if(!templateData.parameters().has(PACKAGE_NAME)) {
        return dialect.sourceFolder;
      }
      final String packageName = templateData.parameters().find(PACKAGE_NAME);
      return ArrayUtils.addAll(dialect.sourceFolder, packageName.split("\\."));
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(PRODUCTION_CODE, false);
    }

  }

  class UnitTest implements RelativeSourcePathResolver {

    @Override
    public String[] resolve(final CodeGenerationContext context, final Dialect dialect, final TemplateData templateData) {
      final String packageName = templateData.parameters().find(PACKAGE_NAME);
      return ArrayUtils.addAll(dialect.testSourceFolder, packageName.split("\\."));
    }

    @Override
    public boolean shouldResolve(final TemplateData templateData) {
      return templateData.parameters().find(UNIT_TEST, false);
    }
  }
}
