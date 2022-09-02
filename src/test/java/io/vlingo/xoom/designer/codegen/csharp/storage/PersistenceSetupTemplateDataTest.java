// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PersistenceSetupTemplateDataTest extends CodeGenerationTest {

  @Test
  public void testWithAdaptersAndProjections() {
    final List<TemplateData> allTemplatesData = StorageTemplateDataFactory.buildWithCqrs("Io.Vlingo.Xoomapp",
        contents(), StorageType.STATE_STORE, ProjectionType.EVENT_BASED);

    //General Assert
    Assertions.assertEquals(8, allTemplatesData.size());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.QUERIES)).count());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.QUERIES_ACTOR)).count());
    Assertions.assertEquals(0, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.PERSISTENCE_SETUP)).count());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).count());

    //Assert for Queries
    final TemplateData queriesTemplateData = allTemplatesData.stream()
        .filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.QUERIES))
        .findFirst().get();

    final TemplateParameters queriesParameters = queriesTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, queriesParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals("IBookQueries", queriesParameters.find(TemplateParameter.QUERIES_NAME));
    Assertions.assertEquals("BookData", queriesParameters.find(TemplateParameter.STATE_DATA_OBJECT_NAME));
    Assertions.assertEquals("bookOf", queriesParameters.find(TemplateParameter.QUERY_BY_ID_METHOD_NAME));
    Assertions.assertEquals("books", queriesParameters.find(TemplateParameter.QUERY_ALL_METHOD_NAME));
    Assertions.assertEquals(1, queriesParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(queriesParameters.hasImport("Io.Vlingo.Xoomapp.Infrastructure"));

    //Assert for QueriesActor
    final TemplateData queriesActorTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.QUERIES_ACTOR)).findFirst().get();

    final TemplateParameters queriesActorParameters =
        queriesActorTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, queriesActorParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals("BookQueriesActor", queriesActorParameters.find(TemplateParameter.QUERIES_ACTOR_NAME));

    //Assert for StoreProvider
    final TemplateData storeProviderData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).findFirst().get();

    Assertions.assertFalse(storeProviderData.isPlaceholder());
    //Assertions.assertTrue(storeProviderData.isPlaceholder());

    //Assert for PersistenceSetup Todo
  }

  private List<Content> contents() {
    return Arrays.asList(
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorRated.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.cs"), null, null, BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookRented.cs"), null, null, BOOK_RENTED_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookPurchased.cs"), null, null, BOOK_PURCHASED_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "IBook.cs"), null, null, BOOK_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.cs"), null, null, BOOK_DATA_CONTENT_TEXT)
    );
  }
  
  private static final String EXPECTED_PACKAGE = "Io.Vlingo.Xoomapp.Infrastructure.Persistence";

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Voomapp", "Model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Voomapp", "Infrastructure").toString();

  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookState { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBook { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_RENTED_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookRented : Event { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_PURCHASED_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookPurchased : Event { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class AuthorData { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_DATA_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure; \\n" +
          "public class BookData { \\n" +
          "... \\n" +
          "}";
}
