// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.designer.task.projectgeneration.code.EndToEndTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@EndToEndTest
public class BasicProjectGenerationTest extends ProjectGenerationTest {

  private static final String bookStoreWithStatefulEntities = "book-store-with-stateful-entities";

  @BeforeAll
  public static void setUp() {
    init();
  }

  @Test
  public void testThatProjectWithScalarTypesIsWorking() {
    generate(bookStoreWithStatefulEntities);
    compile(bookStoreWithStatefulEntities);
    run(bookStoreWithStatefulEntities);
  }

  @Override
  public String modelDirectory() {
    return "book-store-context";
  }

  @AfterAll
  public static void tearDown() throws Exception {
    clear();
  }
}
