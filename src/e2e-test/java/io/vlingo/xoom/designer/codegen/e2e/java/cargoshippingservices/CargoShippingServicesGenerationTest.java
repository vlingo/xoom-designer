// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.RequestAttempt;
import io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaBasedProjectGenerationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager.RABBIT_MQ;
import static io.vlingo.xoom.designer.codegen.e2e.SupportingServicesManager.SCHEMATA;

/**
 * This scenario consists mainly of two services sharing Event schema via
 * XOOM Schemata taking advantage of automatic Schema pull/push provided
 * by the Designer / Schemata integration.
 *
 * See: https://docs.vlingo.io/xoom-designer/development-guide/e2e-tests
 */
public class CargoShippingServicesGenerationTest extends JavaBasedProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    SupportingServicesManager.run();
    init();
  }

  /**
   * Test that the cargo shipping services are generated and working
   */
  @Test
  public void testThatGeneratedServicesAreWorking() {
    assertServiceIsAvailable(SupportingServicesManager.findPortOf(SCHEMATA), "Schemata service is not available");
    assertServiceIsAvailable(SupportingServicesManager.findPortOf(RABBIT_MQ), "RabbitMQ service is not available");

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

    final Predicate<JsonPath> validResponsePreConditionOnNewIncident = res -> res.get("freighterId") !=null && !res.get("freighterId").toString().isEmpty();
    final Predicate<JsonPath> validResponsePreConditionOnRelatedPart = res -> res.getList("relatedParts") != null && !res.getList("relatedParts").isEmpty();

    generateAndRun(freighterMonitoringProject);
    assertThatIncidentIsRegistered(freighterMonitoringProject, newIncident);
    assertThatIncidentIsRetrievedById(freighterMonitoringProject, newIncident, validResponsePreConditionOnNewIncident);
    assertThatIncidentsAreRetrieved(freighterMonitoringProject, newIncident);

    final MechanicalIncidentData incidentWithRelatedPart =
            MechanicalIncidentData.sampleOfIncidentWithRelatedPart(newIncident.id);

    assertThatPartIsRelated(freighterMonitoringProject, incidentWithRelatedPart);
    assertThatIncidentIsRetrievedById(freighterMonitoringProject, incidentWithRelatedPart, validResponsePreConditionOnRelatedPart);
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

  private void assertThatIncidentIsRetrievedById(final Project freighterMonitoringProject,
                                                 final MechanicalIncidentData incident,
                                                 final Predicate<JsonPath> responsePreCondition) {
    final Response response =
            RequestAttempt.of("Incident retrieval by id").acceptResponseWhen(responsePreCondition)
                    .perform(() -> apiOf(freighterMonitoringProject).get("/incidents/" + incident.id));

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
  //TODO: Configure Rabbit MQ exchanges to assert that downstream service properly consumes events
  private void testThatDownstreamServiceIsWorking() {
    final Project freighterMaintenance =
            Project.from("cargo-shipping-context", "freighter-maintenance");

    generateAndRun(freighterMaintenance);
  }

  @AfterEach
  public void stopProject() {
    stopServices();
  }


}
