// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import io.vlingo.xoom.designer.codegen.e2e.DockerServices;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaProjectGenerationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This scenario consists of two services sharing Event schemas and
 * exchange messages respectively via XOOM Schemata and XOOM Lattice/Exchange.
 */
public class CargoShippingServicesGenerationTest extends JavaProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    DockerServices.run();
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
   * - XOOM Annotations + Auto Dispatch
   */
  private void testThatUpstreamServiceIsWorking() {
    final Project freighterMonitoring =
            Project.from("cargo-shipping-context", "freighter-monitoring");

    generateAndRun(freighterMonitoring);
  }

  /**
   * Test that the upstream service is generated and working with:
   * - Stateful Entities containing only String fields
   * - Consumer Exchanges / Schema Download/Transpilation
   * - Operation-based projection
   * - Without annotations or auto-dispatch
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
