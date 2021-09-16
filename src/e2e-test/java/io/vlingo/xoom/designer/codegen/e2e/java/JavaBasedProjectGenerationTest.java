// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.ProjectGenerationTest;

/**
 * See: https://docs.vlingo.io/xoom-designer/development-guide/e2e-tests
 */
public abstract class JavaBasedProjectGenerationTest extends ProjectGenerationTest {

  public static void init() {
    init(CodeElementFormatter.with(Dialect.JAVA, ReservedWordsHandler.usingSuffix("_")));
  }

  protected void compile(final Project project) {
    assertCompilation(JavaCompilation.run(project.generationPath.path).status(), project);
  }

  protected void run(final Project project) {
    JavaAppInitialization.run(project.generationSettings, project.appPort);
    assertInitialization(project);
  }

}
