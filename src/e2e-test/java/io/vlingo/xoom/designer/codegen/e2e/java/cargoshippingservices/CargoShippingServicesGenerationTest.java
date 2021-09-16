// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import io.restassured.response.Response;
import io.vlingo.xoom.designer.codegen.e2e.DockerServices;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaProjectGenerationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.designer.codegen.e2e.DockerServices.RABBIT_MQ;
import static io.vlingo.xoom.designer.codegen.e2e.DockerServices.SCHEMATA;

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
    assertServiceIsAvailable(DockerServices.findPortOf(SCHEMATA), "Schemata service is not available");
    assertServiceIsAvailable(DockerServices.findPortOf(RABBIT_MQ), "RabbitMQ service is not available");

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
    final Project freighterMonitoringProject =
            Project.from("cargo-shipping-context", "freighter-monitoring");

    final MechanicalIncidentData newIncident =
            MechanicalIncidentData.sampleOfNewIncident();

    generateAndRun(freighterMonitoringProject);
    assertThatIncidentIsRegistered(freighterMonitoringProject, newIncident);
    assertThatIncidentIsRetrievedById(freighterMonitoringProject, newIncident);
    assertThatIncidentsAreRetrieved(freighterMonitoringProject, newIncident);

    final MechanicalIncidentData incidentWithRelatedPart =
            MechanicalIncidentData.sampleOfIncidentWithRelatedPart(newIncident.id);

    assertThatPartIsRelated(freighterMonitoringProject, incidentWithRelatedPart);
    assertThatIncidentIsRetrievedById(freighterMonitoringProject, incidentWithRelatedPart);
    assertThatIncidentsAreRetrieved(freighterMonitoringProject, incidentWithRelatedPart);
  }

  private void assertThatIncidentIsRegistered(final Project freighterMonitoringProject,
                                              final MechanicalIncidentData newIncident) {
    final Response response =
            apiOf(freighterMonitoringProject).body(newIncident).post("/incidents");

    final MechanicalIncidentData responseBody =
            response.then().extract().body().as(MechanicalIncidentData.class);

    newIncident.id = responseBody.id;

    Assertions.assertEquals(io.vlingo.xoom.http.Response.Status.Created.code, response.statusCode(), "Wrong http status while registering incident " + freighterMonitoringProject);
    Assertions.assertEquals(newIncident, responseBody, "Wrong response while registering incident " + freighterMonitoringProject);
  }

  private void assertThatPartIsRelated(final Project freighterMonitoringProject,
                                       final MechanicalIncidentData incidentWithRelatedPart) {
    final Response response =
            apiOf(freighterMonitoringProject).body(incidentWithRelatedPart).patch("/incidents/" + incidentWithRelatedPart.id + "/part");

    final MechanicalIncidentData responseBody =
            response.then().extract().body().as(MechanicalIncidentData.class);

    Assertions.assertEquals(io.vlingo.xoom.http.Response.Status.Ok.code, response.statusCode(), "Wrong http status while relating part " + freighterMonitoringProject);
    Assertions.assertEquals(incidentWithRelatedPart, responseBody, "Wrong response while relating part " + freighterMonitoringProject);
  }

  private void assertThatIncidentIsRetrievedById(final Project freighterMonitoringProject, final MechanicalIncidentData incident) {
    final Response response =
            apiOf(freighterMonitoringProject).get("/incidents/" + incident.id);

    final MechanicalIncidentData responseBody =
            response.then().extract().body().as(MechanicalIncidentData.class);

    Assertions.assertEquals(io.vlingo.xoom.http.Response.Status.Ok.code, response.statusCode(), "Wrong http status while retrieving incidents by id " + freighterMonitoringProject);
    Assertions.assertEquals(incident, responseBody,  "Wrong response while retrieving incidents by id " + freighterMonitoringProject);
  }

  private void assertThatIncidentsAreRetrieved(final Project freighterMonitoringProject, final MechanicalIncidentData incident) {
    final Response response =
            apiOf(freighterMonitoringProject).get("/incidents");

    final MechanicalIncidentData[] responseBody =
            response.then().extract().body().as(MechanicalIncidentData[].class);

    Assertions.assertEquals(io.vlingo.xoom.http.Response.Status.Ok.code, response.statusCode(), "Wrong http status while retrieving all " + freighterMonitoringProject);
    Assertions.assertEquals(incident, responseBody[0],  "Wrong response while retrieving all " + freighterMonitoringProject);
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
