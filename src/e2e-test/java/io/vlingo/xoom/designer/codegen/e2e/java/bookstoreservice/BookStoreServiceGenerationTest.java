// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java.bookstoreservice;

import io.restassured.response.Response;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import io.vlingo.xoom.designer.codegen.e2e.java.JavaBasedProjectGenerationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.http.Response.Status;

/**
 * See: https://docs.vlingo.io/xoom-designer/development-guide/e2e-tests
 */
public class BookStoreServiceGenerationTest extends JavaBasedProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    init();
  }

  /**
   * Test that the service is generated and working with:
   * - Stateful Entities containing only scalar-typed fields
   * - Operation-based projection
   * - Xoom Annotations + Auto-dispatch
   */
  @Test
  public void testThatServiceWithStatefulEntitiesIsWorking() {
    final BookData newBook =
            BookData.sampleOfInitialData();

    final BookData bookWithNewPrice =
            BookData.sampleOfChangedPrice();

    final Project projectWithStatefulEntities =
            Project.from("book-store-context", "book-store-with-stateful-entities");

    super.generateAndRun(projectWithStatefulEntities);

    assertThatBookIsCreated(projectWithStatefulEntities, newBook);
    assertThatBookIsRetrievedById(projectWithStatefulEntities, newBook);
    assertThatBooksAreRetrieved(projectWithStatefulEntities, newBook);

    bookWithNewPrice.id = newBook.id;

    assertThatPriceIsChanged(projectWithStatefulEntities, bookWithNewPrice);
    assertThatBookIsRetrievedById(projectWithStatefulEntities, bookWithNewPrice);
    assertThatBooksAreRetrieved(projectWithStatefulEntities, bookWithNewPrice);
  }

  /**
   * Test that the service is generated and working with:
   * - Sourced Entities containing only scalar-typed fields
   * - Event-based projection
   * - Xoom Annotations (w/o Auto Dispatch)
   * - ReactJS app gen
   */
  @Test
  public void testThatServiceWithSourcedEntitiesIsWorking() {
    final BookData newBook =
            BookData.sampleOfInitialData();

    final BookData bookWithNewPrice =
            BookData.sampleOfChangedPrice();

    final Project projectWithStatefulEntities =
            Project.from("book-store-context", "book-store-with-sourced-entities");

    generateAndRun(projectWithStatefulEntities);

    assertThatBookIsCreated(projectWithStatefulEntities, newBook);
    assertThatBookIsRetrievedById(projectWithStatefulEntities, newBook);
    assertThatBooksAreRetrieved(projectWithStatefulEntities, newBook);

    bookWithNewPrice.id = newBook.id;

    assertThatPriceIsChanged(projectWithStatefulEntities, bookWithNewPrice);
    assertThatBookIsRetrievedById(projectWithStatefulEntities, bookWithNewPrice);
    assertThatBooksAreRetrieved(projectWithStatefulEntities, bookWithNewPrice);
  }

  private void assertThatBookIsCreated(final Project bookStoreProject, final BookData newBook) {
    final Response response =
            apiOf(bookStoreProject).body(newBook).post("/books");

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    newBook.id = responseBody.id;

    Assertions.assertEquals(Status.Created.code, response.statusCode(), "Wrong http status while creating book " + bookStoreProject);
    Assertions.assertEquals(newBook, responseBody, "Wrong response while creating book " + bookStoreProject);
  }

  private void assertThatBookIsRetrievedById(final Project bookStoreProject, final BookData book) {
    final Response response =
            apiOf(bookStoreProject).get("/books/" + book.id);

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while retrieving book by id " + bookStoreProject);
    Assertions.assertEquals(book, responseBody,  "Wrong response while retrieving book by id " + bookStoreProject);
  }

  private void assertThatBooksAreRetrieved(final Project bookStoreProject, final BookData newBook) {
    final Response response =
            apiOf(bookStoreProject).get("/books");

    final BookData[] responseBody =
            response.then().extract().body().as(BookData[].class);

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while retrieving all " + bookStoreProject);
    Assertions.assertEquals(newBook, responseBody[0],  "Wrong response while retrieving all " + bookStoreProject);
  }

  private void assertThatPriceIsChanged(final Project bookStoreProject, final BookData bookWithNewPrice) {
    final Response response =
            apiOf(bookStoreProject).body(bookWithNewPrice).patch("/books/" + bookWithNewPrice.id + "/price");

    final BookData responseBody =
            response.then().extract().body().as(BookData.class);

    bookWithNewPrice.id = responseBody.id;

    Assertions.assertEquals(Status.Ok.code, response.statusCode(), "Wrong http status while changing price " + bookStoreProject);
    Assertions.assertEquals(bookWithNewPrice, responseBody, "Wrong response while changing price " + bookStoreProject);
  }

  @AfterAll
  public static void tearDown() throws Exception {
    clear();
  }
}
