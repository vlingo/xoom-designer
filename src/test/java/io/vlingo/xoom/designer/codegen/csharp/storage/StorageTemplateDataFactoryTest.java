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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StorageTemplateDataFactoryTest extends CodeGenerationTest {

  @Test
  public void testStorageTemplateDataOnSourcedSingleModel() {
    final List<TemplateData> allTemplatesData = StorageTemplateDataFactory.build("Io.Vlingo.Xoomapp", contents(),
        StorageType.JOURNAL, ProjectionType.EVENT_BASED, "Xoomapp", databaseTypes());

    Assertions.assertEquals(4, allTemplatesData.size());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).count());
    Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).count());

    final TemplateData entryAdapterTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).findFirst().get();

    final TemplateParameters stateAdapterConfigurationParameters =
        entryAdapterTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals("BookRented", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
    Assertions.assertEquals(StorageType.JOURNAL, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
    Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("Io.vlingo.Xoomapp.Model.Book"));
    Assertions.assertEquals("BookRentedAdapter", entryAdapterTemplateData.filename());

    final TemplateData storeProviderTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).findFirst().get();

    final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals(Model.DOMAIN, storeProviderParameters.find(TemplateParameter.MODEL));
    Assertions.assertEquals("JournalProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
    Assertions.assertEquals(1, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(storeProviderParameters.hasImport("Io.vlingo.Xoomapp.Model.Book"));
    Assertions.assertEquals("BookRented", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
    Assertions.assertEquals("BookRentedAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
    Assertions.assertEquals("BookPurchased", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
    Assertions.assertEquals("BookPurchasedAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
    Assertions.assertEquals(2, storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).size());
    Assertions.assertTrue(storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).contains("AuthorEntity"));
    Assertions.assertTrue(storeProviderParameters.<Set<String>>find(TemplateParameter.AGGREGATES).contains("BookEntity"));
    Assertions.assertEquals("JournalProvider", storeProviderTemplateData.filename());
  }

  @Test
  public void testStorageTemplateDataOnStatefulSingleModel() {
    final List<TemplateData> allTemplatesData = StorageTemplateDataFactory.build("Io.Vlingo.Xoomapp", contents(),
        StorageType.STATE_STORE, ProjectionType.EVENT_BASED, "Xoomapp", databaseTypes());

    Assertions.assertEquals(4, allTemplatesData.size());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).count());
    Assertions.assertEquals(1, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).count());

    final TemplateData stateAdapterTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).findFirst().get();

    final TemplateParameters stateAdapterConfigurationParameters =
        stateAdapterTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals("BookState", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
    Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
    Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("Io.vlingo.Xoomapp.Model.Book"));
    Assertions.assertEquals("BookStateAdapter", stateAdapterTemplateData.filename());

    final TemplateData storeProviderTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).findFirst().get();

    final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals(Model.DOMAIN, storeProviderParameters.find(TemplateParameter.MODEL));
    Assertions.assertEquals("StateStoreProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
    Assertions.assertEquals(2, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(storeProviderParameters.hasImport("Io.Vlingo.Xoomapp.Model.Author"));
    Assertions.assertEquals("BookState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
    Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
    Assertions.assertEquals("AuthorState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
    Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
    Assertions.assertEquals("StateStoreProvider", storeProviderTemplateData.filename());
  }

  @Test
  public void testStorageTemplateDataOnStatefulCQRSModel() {
    final List<TemplateData> allTemplatesData = StorageTemplateDataFactory.buildWithCqrs("Io.Vlingo.Xoomapp", contents(),
            StorageType.STATE_STORE, ProjectionType.NONE, "Xoomapp", databaseTypes());

    //General Assert

    Assertions.assertEquals(9, allTemplatesData.size());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).count());
    Assertions.assertEquals(2, allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER)).count());

    //Assert for StateAdapter

    final TemplateData stateAdapterTemplateData =
        allTemplatesData.stream().filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.ADAPTER)).findFirst().get();

    final TemplateParameters stateAdapterConfigurationParameters =
        stateAdapterTemplateData.parameters();

    Assertions.assertEquals(EXPECTED_PACKAGE, stateAdapterConfigurationParameters.find(TemplateParameter.PACKAGE_NAME));
    Assertions.assertEquals("BookState", stateAdapterConfigurationParameters.find(TemplateParameter.SOURCE_NAME));
    Assertions.assertEquals(StorageType.STATE_STORE, stateAdapterConfigurationParameters.find(TemplateParameter.STORAGE_TYPE));
    Assertions.assertEquals(1, stateAdapterConfigurationParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
    Assertions.assertTrue(stateAdapterConfigurationParameters.hasImport("Io.vlingo.Xoomapp.Model.Book"));
    Assertions.assertEquals("BookStateAdapter", stateAdapterTemplateData.filename());

    //Assert for StoreProvider

    final List<TemplateData> storeProviders =
        allTemplatesData.stream()
            .filter(templateData -> templateData.hasStandard(CsharpTemplateStandard.STORE_PROVIDER))
            .collect(Collectors.toList());

    IntStream.range(0, 1).forEach(modelClassificationIndex -> {
      final TemplateData storeProviderTemplateData = storeProviders.get(modelClassificationIndex);
      final Model model = modelClassificationIndex == 0 ? Model.COMMAND : Model.QUERY;
      final TemplateParameters storeProviderParameters = storeProviderTemplateData.parameters();
      final int expectedImports = modelClassificationIndex == 0 ? 2 : 1;
      Assertions.assertEquals(EXPECTED_PACKAGE, storeProviderParameters.find(TemplateParameter.PACKAGE_NAME));
      Assertions.assertEquals(model, storeProviderParameters.find(TemplateParameter.MODEL));
      Assertions.assertEquals(model.title + "StateStoreProvider", storeProviderParameters.find(TemplateParameter.STORE_PROVIDER_NAME));
      Assertions.assertEquals(expectedImports, storeProviderParameters.<Set<ImportParameter>>find(TemplateParameter.IMPORTS).size());
      Assertions.assertTrue(storeProviderParameters.hasImport("Io.Vlingo.Xoomapp.Model.Author"));
      Assertions.assertEquals("BookState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getSourceClass());
      Assertions.assertEquals("BookStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(0).getAdapterClass());
      Assertions.assertEquals("AuthorState", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getSourceClass());
      Assertions.assertEquals("AuthorStateAdapter", storeProviderParameters.<List<Adapter>>find(TemplateParameter.ADAPTERS).get(1).getAdapterClass());
      Assertions.assertEquals(model.title + "StateStoreProvider", storeProviderTemplateData.filename());
    });
  }

  private List<Content> contents() {
    return Arrays.asList(
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorState.cs"), null, null, AUTHOR_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_STATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookState.cs"), null, null, BOOK_STATE_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookRented.cs"), null, null, BOOK_RENTED_TEXT),
        Content.with(CsharpTemplateStandard.DOMAIN_EVENT, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookPurchased.cs"), null, null, BOOK_PURCHASED_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "IAuthor.cs"), null, null, AUTHOR_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE_PROTOCOL, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "IBook.cs"), null, null, BOOK_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "author").toString(), "AuthorEntity.cs"), null, null, AUTHOR_ENTITY_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.AGGREGATE, new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "book").toString(), "BookEntity.cs"), null, null, BOOK_ENTITY_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.PROJECTION_DISPATCHER_PROVIDER, new OutputFile(PERSISTENCE_PACKAGE_PATH, "ProjectionDispatcherProvider.cs"), null, null, PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "AuthorData.cs"), null, null, AUTHOR_DATA_CONTENT_TEXT),
        Content.with(CsharpTemplateStandard.DATA_OBJECT, new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "BookData.cs"), null, null, BOOK_DATA_CONTENT_TEXT)
    );
  }

  private static final Map<Model, DatabaseType> databaseTypes() {
    return new HashMap<Model, DatabaseType>() {
      private static final long serialVersionUID = 1L;
      {
        put(Model.COMMAND, DatabaseType.IN_MEMORY);
        put(Model.QUERY, DatabaseType.IN_MEMORY);
      }};
  }

  private static final String EXPECTED_PACKAGE = "Io.Vlingo.Xoomapp.Infrastructure.Persistence";

  private static final String PROJECT_PATH =
      OperatingSystem.detect().isWindows() ?
          Paths.get("D:\\projects", "xoom-app").toString() :
          Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Model").toString();

  private static final String PERSISTENCE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure", "Persistence").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH =
      Paths.get(PROJECT_PATH, "Io.Vlingo.Xoomapp", "Infrastructure").toString();

  private static final String AUTHOR_STATE_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public class AuthorState { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_STATE_CONTENT_TEXT =
      "namespace Io.vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookState { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthor { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_CONTENT_TEXT =
      "namespace Io.vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBook { \\n" +
          "... \\n" +
          "}";

  private static final String AUTHOR_ENTITY_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Model.Author; \\n" +
          "public interface IAuthorEntity { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_ENTITY_CONTENT_TEXT =
      "namespace Io.vlingo.Xoomapp.Model.Book; \\n" +
          "public interface IBookEntity { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_RENTED_TEXT =
      "namespace Io.vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookRented : Event { \\n" +
          "... \\n" +
          "}";

  private static final String BOOK_PURCHASED_TEXT =
      "namespace Io.vlingo.Xoomapp.Model.Book; \\n" +
          "public class BookPurchased : Event { \\n" +
          "... \\n" +
          "}";

  private static final String PROJECTION_DISPATCHER_PROVIDER_CONTENT_TEXT =
      "namespace Io.Vlingo.Xoomapp.Infrastructure.persistence; \\n" +
          "public class ProjectionDispatcherProvider { \\n" +
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
