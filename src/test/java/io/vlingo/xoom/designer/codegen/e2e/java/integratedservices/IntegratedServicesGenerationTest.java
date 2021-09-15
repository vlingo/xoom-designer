// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.integratedservices;

import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaProjectGenerationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntegratedServicesGenerationTest extends JavaProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    init();
  }

  /**
   * Test that the services are generated and working
   */
  @Test
  public void testThatGeneratedServicesAreWorking() {
    testThatUpstreamServiceIsWorking();
    testThatDownstreamServiceIsWorking();
  }

  /**
   * Test that the upstream service is generated and working with:
   * - Sourced Entities containing Value Objects and Custom-typed collections
   * - Collection mutation (addition)
   * - Producer Exchanges / Schema Registration
   * - Event-based projection
   * - Without annotations
   */
  private void testThatUpstreamServiceIsWorking() {
    final Project freighterMonitoring =
            Project.from("cargo-shipping-context", "freighter-monitoring");

    generateAndRun(freighterMonitoring);
  }

  /**
   * Test that the upstream service is generated and working with:
   * - Stateful Entities containing Value Objects and Custom-typed collections
   * - Consumer Exchanges / Schema Download/Transpilation
   * - Operation-based projection
   * - Without annotations
   */
  private void testThatDownstreamServiceIsWorking() {
    final Project freighterMaintenance =
            Project.from("cargo-shipping-context", "freighter-maintenance");

    generateAndRun(freighterMaintenance);
  }

  @AfterAll
  public static void tearDown() throws Exception {
    clear();
  }

}
