// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.projections.ProjectionType;
import io.vlingo.xoom.turbo.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.turbo.codegen.content.Content;
import io.vlingo.xoom.turbo.codegen.content.ContentQuery;
import io.vlingo.xoom.turbo.codegen.template.TemplateData;
import io.vlingo.xoom.turbo.codegen.template.TemplateParameters;
import io.vlingo.xoom.turbo.codegen.template.TemplateStandard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.STORE_PROVIDER;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.TemplateParameter.*;
import static java.util.stream.Collectors.toSet;

public class StorageProviderTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public static List<TemplateData> from(final String persistencePackage,
                                        final StorageType storageType,
                                        final ProjectionType projectionType,
                                        final List<TemplateData> stateAdaptersTemplateData,
                                        final List<Content> contents,
                                        final Stream<Model> models,
                                        final boolean useAnnotation) {
    return models.sorted().filter(model -> supportModel(model, useAnnotation))
            .map(model -> new StorageProviderTemplateData(persistencePackage, storageType,
                    projectionType, stateAdaptersTemplateData, contents, useAnnotation, model))
            .collect(Collectors.toList());
  }

  protected StorageProviderTemplateData(final String persistencePackage,
                                        final StorageType storageType,
                                        final ProjectionType projectionType,
                                        final List<TemplateData> stateAdaptersTemplateData,
                                        final List<Content> contents,
                                        final boolean useAnnotation,
                                        final Model model) {
    this.templateParameters =
            loadParameters(persistencePackage, storageType, projectionType,
                    stateAdaptersTemplateData, contents, useAnnotation, model);
  }

  private TemplateParameters loadParameters(final String packageName,
                                            final StorageType storageType,
                                            final ProjectionType projectionType,
                                            final List<TemplateData> templatesData,
                                            final List<Content> contents,
                                            final boolean useAnnotation,
                                            final Model model) {
    final List<Adapter> adapters = Adapter.from(templatesData);
    final List<Queries> queries = Queries.from(model, contents, templatesData);
    final Stream<String> persistentTypes = storageType.findPersistentQualifiedTypes(model, contents).stream();
    final Set<String> imports = resolveImports(model, storageType, contents, queries);

    return TemplateParameters.with(STORAGE_TYPE, storageType).and(PROJECTION_TYPE, projectionType)
            .and(MODEL, model).and(PACKAGE_NAME, packageName)
            .and(REQUIRE_ADAPTERS, storageType.requireAdapters(model))
            .and(USE_PROJECTIONS, projectionType.isProjectionEnabled())
            .and(ADAPTERS, adapters).and(QUERIES, queries)
            .and(AGGREGATES, ContentQuery.findClassNames(AGGREGATE, contents))
            .and(PERSISTENT_TYPES, persistentTypes.map(CodeElementFormatter::simpleNameOf).collect(toSet()))
            .andResolve(STORE_PROVIDER_NAME, STORE_PROVIDER::resolveClassname)
            .and(USE_ANNOTATIONS, useAnnotation)
            .addImports(imports);
  }

  private Set<String> resolveImports(final Model model,
                                     final StorageType storageType,
                                     final List<Content> contents,
                                     final List<Queries> queries) {
    final Set<String> sourceClassQualifiedNames =
            storageType.resolveAdaptersQualifiedName(model, contents);

    final Set<String> persistentTypes =
            storageType.findPersistentQualifiedTypes(model, contents);

    final Set<String> queriesQualifiedNames = queries.stream()
            .flatMap(param -> param.getQualifiedNames().stream())
            .collect(toSet());

    final Set<String> aggregateActorQualifiedNames = storageType.isSourced() ?
            ContentQuery.findFullyQualifiedClassNames(AGGREGATE, contents) : new HashSet<>();

    return Stream.of(sourceClassQualifiedNames, queriesQualifiedNames,
            aggregateActorQualifiedNames, persistentTypes).flatMap(s -> s.stream())
            .collect(Collectors.toSet());
  }

  private static boolean supportModel(final Model model, final boolean useAnnotation) {
    if (useAnnotation) {
      return model.isQueryModel();
    }
    return true;
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return STORE_PROVIDER;
  }

  @Override
  public boolean isPlaceholder() {
    return templateParameters.find(USE_ANNOTATIONS);
  }
}
