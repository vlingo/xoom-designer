// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e.java;

import io.restassured.response.Response;
import io.vlingo.xoom.designer.codegen.e2e.Project;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.http.Response.Status;

public class BasicProjectGenerationTest extends JavaProjectGenerationTest {

  @BeforeAll
  public static void setUp() {
    init();
  }

  @Test
  public void testThatProjectWithScalarTypesIsWorking() {
    final BookData newBook =
            BookData.sampleOfInitialData();

    final BookData bookWithNewPrice =
            BookData.sampleOfChangedPrice();

    final Project projectWithStatefulEntities =
            Project.from("book-store-context", "book-store-with-stateful-entities");

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

  public static class BookData {
    public String id;
    public final String title;
    public final int stockCode;
    public final byte publicCode;
    public final double price;
    public final float weight;
    public final long height;
    public final boolean available;
    public final char symbol;

    public static BookData sampleOfInitialData() {
      return new BookData("", "IDDD", 987, (byte) 1, 457.25, 150, 10, true, 'a');
    }

    public static BookData sampleOfChangedPrice() {
      return new BookData("", "IDDD", 987, (byte) 1, 478.25, 150, 10, true, 'a');
    }

    private BookData(final String id,
                     final String title,
                     final int stockCode,
                     final byte publicCode,
                     final double price,
                     final float weight,
                     final long height,
                     final boolean available,
                     final char symbol) {
      this.id = id;
      this.title = title;
      this.stockCode = stockCode;
      this.publicCode = publicCode;
      this.price = price;
      this.weight = weight;
      this.height = height;
      this.available = available;
      this.symbol = symbol;
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      BookData another = (BookData) other;
      return new EqualsBuilder()
              .append(this.title, another.title)
              .append(this.stockCode, another.stockCode)
              .append(this.publicCode, another.publicCode)
              .append(this.price, another.price)
              .append(this.weight, another.weight)
              .append(this.height, another.height)
              .append(this.available, another.available)
              .append(this.symbol, another.symbol)
              .isEquals();
    }
  }

  @AfterAll
  public static void tearDown() throws Exception {
    clear();
  }
}
