// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.matchingservice;

import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaBasedProjectGenerationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager.RABBIT_MQ;
import static io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager.SCHEMATA;

/**
 * This scenario consists of a service containing complex values objects
 * and command methods accepting many combinations of parameters.
 *
 * See: https://docs.vlingo.io/xoom-designer/development-guide/e2e-tests
 */
public class MatchingServiceGenerationTest extends JavaBasedProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    SupportingServicesManager.run();
    init();
  }

  /**
   * Test that the upstream service is generated and working with:
   * - Sourced Entities containing Value Objects
   * - Producer Exchanges / Schema Registration
   * - Event-based projection
   * - Without Annotations and Auto Dispatch
   */
  @Test
  public void testThatGeneratedServicesAreWorking() {
    assertServiceIsAvailable(SupportingServicesManager.findPortOf(SCHEMATA), "Schemata service is not available");
    assertServiceIsAvailable(SupportingServicesManager.findPortOf(RABBIT_MQ), "RabbitMQ service is not available");

    final Project matchingServiceWithSourcedEntities =
            Project.from("matching-context", "matching-with-sourced-entities");

    generateAndRun(matchingServiceWithSourcedEntities);
  }

  @AfterAll
  public static void tearDown() throws Exception {
    clear();
  }

}
