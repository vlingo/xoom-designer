// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.resource;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.TextExpectation;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.designer.codegen.CodeGenerationTest;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.turbo.OperatingSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class RestResourceWithCompositeIdGenerationStepTest extends CodeGenerationTest {

  @Test
  public void testThatRestResourceIsGenerated() {
    final CodeGenerationParameter packageParameter = CodeGenerationParameter.of(Label.PACKAGE, "io.vlingo.xoomapp");

    final CodeGenerationParameter useCQRSParameter = CodeGenerationParameter.of(Label.CQRS, "true");

    final CodeGenerationParameter dialect = CodeGenerationParameter.of(Label.DIALECT, Dialect.JAVA);

    final CodeGenerationParameters parameters = CodeGenerationParameters.from(packageParameter, dialect,
        useCQRSParameter, catalogAggregate(), nameValueObject());

    final CodeGenerationContext context = CodeGenerationContext.with(parameters).contents(contents());

    new RestResourceGenerationStep().process(context);

    final Content catalogResource = context.findContent(JavaTemplateStandard.REST_RESOURCE, "CatalogResource");

    Assertions.assertEquals(9, context.contents().size());
    Assertions.assertTrue(catalogResource.contains(TextExpectation.onJava().read("catalog-rest-resource-with-composite-id")));
  }

  private CodeGenerationParameter catalogAggregate() {
    final CodeGenerationParameter idField = CodeGenerationParameter.of(Label.STATE_FIELD, "id")
        .relate(Label.FIELD_TYPE, "String");

    final CodeGenerationParameter nameField = CodeGenerationParameter.of(Label.STATE_FIELD, "name")
        .relate(Label.FIELD_TYPE, "Name");

    final CodeGenerationParameter authorIdField = CodeGenerationParameter.of(Label.STATE_FIELD, "authorId")
        .relate(Label.FIELD_TYPE, "CompositeId");

    final CodeGenerationParameter catalogCreatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "CatalogCreated")
        .relate(idField).relate(authorIdField).relate(nameField);

    final CodeGenerationParameter catalogUpdatedEvent = CodeGenerationParameter
        .of(Label.DOMAIN_EVENT, "CatalogUpdated")
        .relate(idField).relate(authorIdField).relate(nameField);

    final CodeGenerationParameter factoryMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "withName")
        .relate(Label.METHOD_PARAMETER, "authorId")
        .relate(Label.METHOD_PARAMETER, "name")
        .relate(Label.FACTORY_METHOD, "true")
        .relate(catalogCreatedEvent);

    final CodeGenerationParameter updateMethod = CodeGenerationParameter.of(Label.AGGREGATE_METHOD, "update")
        .relate(Label.METHOD_PARAMETER, "id")
        .relate(Label.METHOD_PARAMETER, "authorId")
        .relate(Label.METHOD_PARAMETER, "name")
        .relate(catalogUpdatedEvent);

    final CodeGenerationParameter withNameRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "withName")
        .relate(Label.ROUTE_METHOD, "POST")
        .relate(Label.ROUTE_PATH, "/authors/{authorId}/catalogs");

    final CodeGenerationParameter updateRoute = CodeGenerationParameter.of(Label.ROUTE_SIGNATURE, "update")
        .relate(Label.ROUTE_METHOD, "PATCH")
        .relate(Label.ROUTE_PATH, "/authors/{authorId}/catalogs/{id}")
        .relate(Label.REQUIRE_ENTITY_LOADING, "true");

    return CodeGenerationParameter.of(Label.AGGREGATE, "Catalog")
        .relate(Label.URI_ROOT, "/authors/{authorId}/catalogs").relate(idField)
        .relate(nameField).relate(authorIdField)
        .relate(catalogCreatedEvent).relate(catalogUpdatedEvent)
        .relate(factoryMethod).relate(updateMethod)
        .relate(withNameRoute).relate(updateRoute);
  }

  private CodeGenerationParameter nameValueObject() {
    return CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
            .relate(Label.FIELD_TYPE, "String"))
        .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
            .relate(Label.FIELD_TYPE, "String"));
  }

  private Content[] contents() {
    return new Content[] {
        Content.with(JavaTemplateStandard.AGGREGATE_PROTOCOL,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "catalog").toString(), "Catalog.java"), null, null,
            CATALOG_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.AGGREGATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "catalog").toString(), "CatalogEntity.java"), null, null,
            CATALOG_AGGREGATE_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.AGGREGATE_STATE,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH, "catalog").toString(), "CatalogState.java"), null, null,
            CATALOG_STATE_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.VALUE_OBJECT,
            new OutputFile(Paths.get(MODEL_PACKAGE_PATH).toString(), "Name.java"), null, null,
            NAME_VALUE_OBJECT_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.DATA_OBJECT,
            new OutputFile(Paths.get(INFRASTRUCTURE_PACKAGE_PATH).toString(), "CatalogData.java"), null, null,
            CATALOG_DATA_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.QUERIES,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "CatalogQueries.java"), null, null,
            CATALOG_QUERIES_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.QUERIES_ACTOR,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "CatalogQueriesActor.java"), null, null,
            CATALOG_QUERIES_ACTOR_CONTENT_TEXT),
        Content.with(JavaTemplateStandard.STORE_PROVIDER,
            new OutputFile(Paths.get(PERSISTENCE_PACKAGE_PATH).toString(), "QueryModelStateStoreProvider.java"), null,
            null, QUERY_MODEL_STORE_PROVIDER_CONTENT)
    };
  }

  private static final String PROJECT_PATH = OperatingSystem.detect().isWindows()
      ? Paths.get("D:\\projects", "xoom-app").toString()
      : Paths.get("/home", "xoom-app").toString();

  private static final String MODEL_PACKAGE_PATH = Paths.get(PROJECT_PATH, "src", "main", "java",
      "io", "vlingo", "xoomapp", "model").toString();

  private static final String INFRASTRUCTURE_PACKAGE_PATH = Paths.get(PROJECT_PATH, "src", "main", "java",
      "io", "vlingo", "xoomapp", "infrastructure").toString();

  private static final String PERSISTENCE_PACKAGE_PATH = Paths.get(INFRASTRUCTURE_PACKAGE_PATH, "persistence")
      .toString();

  private static final String CATALOG_CONTENT_TEXT = "package io.vlingo.xoomapp.model.catalog; \\n" +
      "public interface Catalog { \\n" +
      "... \\n" +
      "}";

  private static final String CATALOG_STATE_CONTENT_TEXT = "package io.vlingo.xoomapp.model.catalog; \\n" +
      "public class CatalogState { \\n" +
      "... \\n" +
      "}";

  private static final String CATALOG_AGGREGATE_CONTENT_TEXT = "package io.vlingo.xoomapp.model.catalog; \\n" +
      "public class CatalogEntity { \\n" +
      "... \\n" +
      "}";

  private static final String CATALOG_DATA_CONTENT_TEXT = "package io.vlingo.xoomapp.infrastructure; \\n" +
      "public class CatalogData { \\n" +
      "... \\n" +
      "}";

  private static final String NAME_VALUE_OBJECT_CONTENT_TEXT = "package io.vlingo.xoomapp.model; \\n" +
      "public class Name { \\n" +
      "... \\n" +
      "}";

  private static final String CATALOG_QUERIES_CONTENT_TEXT = "package io.vlingo.xoomapp.infrastructure.persistence; \\n"
      +
      "public interface CatalogQueries { \\n" +
      "... \\n" +
      "}";

  private static final String CATALOG_QUERIES_ACTOR_CONTENT_TEXT = "package io.vlingo.xoomapp.infrastructure.persistence; \\n"
      +
      "public class CatalogQueriesActor { \\n" +
      "... \\n" +
      "}";

  private static final String QUERY_MODEL_STORE_PROVIDER_CONTENT = "package io.vlingo.xoomapp.infrastructure.persistence; \\n"
      +
      "public class QueryModelStateStoreProvider { \\n" +
      "... \\n" +
      "}";

}
