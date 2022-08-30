// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.TemplateParameter;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class StorageProviderTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public static List<TemplateData> from(final String persistencePackage, final StorageType storageType,
                                        final ProjectionType projectionType,
                                        final List<TemplateData> stateAdaptersTemplateData,
                                        final List<Content> contents, final Stream<Model> models) {
    return models.sorted()
        .map(model -> new StorageProviderTemplateData(persistencePackage, storageType, projectionType,
            stateAdaptersTemplateData, contents, model))
        .collect(Collectors.toList());
  }

  protected StorageProviderTemplateData(final String persistencePackage, final StorageType storageType,
                                        final ProjectionType projectionType,
                                        final List<TemplateData> stateAdaptersTemplateData,
                                        final List<Content> contents, final Model model) {
    this.templateParameters = loadParameters(persistencePackage, storageType, projectionType,
                    stateAdaptersTemplateData, contents, model);
  }

  private TemplateParameters loadParameters(final String packageName, final StorageType storageType,
                                            final ProjectionType projectionType,
                                            final List<TemplateData> templatesData,
                                            final List<Content> contents, final Model model) {
    final List<Adapter> adapters = Adapter.from(templatesData);
    final List<Queries> queries = Queries.from(model, contents, templatesData);
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final Stream<String> persistentTypes = storageType.findPersistentQualifiedTypes(model, contents).stream();
    final Set<String> imports = resolveImports(model, storageType, contents, queries);

    return TemplateParameters.with(TemplateParameter.PACKAGE_NAME, packageName)
        .and(TemplateParameter.STORAGE_TYPE, storageType)
        .and(TemplateParameter.PROJECTION_TYPE, projectionType)
        .and(TemplateParameter.MODEL, model)
        .and(TemplateParameter.REQUIRE_ADAPTERS, storageType.requireAdapters(model))
        .and(TemplateParameter.USE_PROJECTIONS, projectionType.isProjectionEnabled())
        .and(TemplateParameter.ADAPTERS, adapters)
        .and(TemplateParameter.QUERIES, queries)
        .and(TemplateParameter.AGGREGATES, ContentQuery.findClassNames(CsharpTemplateStandard.AGGREGATE, contents))
        .and(TemplateParameter.PERSISTENT_TYPES, persistentTypes.map(codeElementFormatter::simpleNameOf).collect(toSet()))
        .andResolve(TemplateParameter.STORE_PROVIDER_NAME, CsharpTemplateStandard.STORE_PROVIDER::resolveClassname)
        .addImports(imports);
  }

  private Set<String> resolveImports(final Model model, final StorageType storageType, final List<Content> contents,
                                     final List<Queries> queries) {
    final Set<String> sourceClassQualifiedNames = storageType.resolveAdaptersQualifiedName(model, contents);

    final Set<String> persistentTypes = storageType.findPersistentQualifiedTypes(model, contents);

    final Set<String> queriesQualifiedNames = queries.stream()
            .flatMap(param -> param.getQualifiedNames().stream())
            .collect(toSet());

    final Set<String> aggregateActorQualifiedNames = storageType.isSourced() ?
            ContentQuery.findFullyQualifiedClassNames(CsharpTemplateStandard.AGGREGATE, contents) : new HashSet<>();

    return Stream.of(sourceClassQualifiedNames, queriesQualifiedNames, aggregateActorQualifiedNames, persistentTypes)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return CsharpTemplateStandard.STORE_PROVIDER;
  }

}
