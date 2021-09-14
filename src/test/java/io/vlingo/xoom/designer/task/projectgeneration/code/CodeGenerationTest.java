// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import io.vlingo.xoom.turbo.ComponentRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class CodeGenerationTest {

  @BeforeEach
  public void setUp() {
    ComponentRegistry.register("defaultCodeFormatter", CodeElementFormatter.with(Dialect.findDefault(), ReservedWordsHandler.usingSuffix("_")));
  }

  @AfterEach
  public void tearDown() {
    ComponentRegistry.clear();
  }
}
