// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectGenerationContext {

  private final Logger logger;
  public final String generationId;
  private final CodeGenerationContext codeGenerationContext;
  private final Map<ProjectGenerationOutput, Object> output = new HashMap<>();

  private ProjectGenerationContext() {
    this.generationId = UUID.randomUUID().toString();
    this.codeGenerationContext = CodeGenerationContext.empty();
    this.logger = Logger.basicLogger();
  }

  public CodeGenerationContext codeGenerationContext() {
    return codeGenerationContext;
  }

}
