// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.bookstoreservice;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.RequestAttempt;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaBasedProjectGenerationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Predicate;

import static io.vlingo.xoom.http.Response.Status;

/**
 * See: https://docs.vlingo.io/xoom-designer/development-guide/e2e-tests
 */
public class BookStoreWithCompositeIdServiceGenerationTest extends JavaBasedProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    init();
  }
  /**
   * Test that the service is generated and working with:
   * - CompositeId
   * - Sourced Entities containing only scalar-typed fields
   * - Event-based projection
   * - Xoom Annotations
   * - ReactJS app gen
   */
  @Test
  public void testThatServiceIsWorking() {
    final BookData newBook =
        BookData.sampleOfInitialData();

    final BookData bookWithNewPrice =
        BookData.sampleOfChangedPrice();

    final Project projectCompositeId =
        Project.from("book-store-context", "book-store-with-composite-id");

    generateAndRun(projectCompositeId);

    final Predicate<JsonPath> validResponsePreConditionOnNewBook =
        res -> res.get("title") != null && !res.get("title").toString().isEmpty();

    final Predicate<JsonPath> validResponsePreConditionOnChangedPrice =
        res -> res.get("price") != null && !res.get("price").toString().equals(String.valueOf(newBook.price));

    assertThatBookIsCreated(projectCompositeId, newBook);
    assertThatBookIsRetrievedById(projectCompositeId, newBook, validResponsePreConditionOnNewBook);
    assertThatBooksAreRetrieved(projectCompositeId, newBook);

    bookWithNewPrice.id = newBook.id;

    assertThatPriceIsChanged(projectCompositeId, bookWithNewPrice);
    assertThatBookIsRetrievedById(projectCompositeId, bookWithNewPrice, validResponsePreConditionOnChangedPrice);
    assertThatBooksAreRetrieved(projectCompositeId, bookWithNewPrice);
  }

  private void assertThatBookIsCreated(final Project bookStoreProject, final BookData newBook) {
    final Response response =
            apiOf(bookStoreProject).body(newBook).post("/authors/"+ UUID.randomUUID() +"/books");

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    newBook.id = responseBody.id;

    Assertions.assertEquals(Status.Created.code, response.statusCode(), "Wrong http status while creating book " + bookStoreProject);
    Assertions.assertEquals(newBook, responseBody, "Wrong response while creating book " + bookStoreProject);
  }

  private void assertThatBookIsRetrievedById(final Project bookStoreProject, final BookData book, final Predicate<JsonPath> responsePreCondition) {
    final Response response =
            RequestAttempt.of("Book retrieval by id").acceptResponseWhen(responsePreCondition)
                    .perform(() -> apiOf(bookStoreProject).get("/authors/"+ UUID.randomUUID() +"/books/" + book.id));

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while retrieving book by id " + bookStoreProject);
    Assertions.assertEquals(book, responseBody,  "Wrong response while retrieving book by id " + bookStoreProject);
  }

  private void assertThatBooksAreRetrieved(final Project bookStoreProject, final BookData newBook) {
    final Response response =
            apiOf(bookStoreProject).get("/authors/"+ UUID.randomUUID() +"/books");

    final BookData[] responseBody =
            response.then().extract().body().as(BookData[].class);

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while retrieving all " + bookStoreProject);
    Assertions.assertEquals(newBook, responseBody[0],  "Wrong response while retrieving all " + bookStoreProject);
  }

  private void assertThatPriceIsChanged(final Project bookStoreProject, final BookData bookWithNewPrice) {
    final Response response =
            apiOf(bookStoreProject).body(bookWithNewPrice).patch("/authors/"+ UUID.randomUUID() +"/books/" + bookWithNewPrice.id + "/price");

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    bookWithNewPrice.id = responseBody.id;

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while changing price " + bookStoreProject);
    Assertions.assertEquals(bookWithNewPrice, responseBody, "Wrong response while changing price " + bookStoreProject);
  }

  @AfterEach
  public void stopProject() {
    stopServices();
  }

}
