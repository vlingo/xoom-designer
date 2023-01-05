// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistenceSetupTemplateData extends TemplateData {

  private final TemplateParameters templateParameters;

  public static TemplateData from(final String basePackage,
                                  final String persistencePackage,
                                  final Boolean useCQRS,
                                  final StorageType storageType,
                                  final ProjectionType projectionType,
                                  final List<TemplateData> templatesData,
                                  final List<Content> contents) {
    return new PersistenceSetupTemplateData(basePackage, persistencePackage, useCQRS,
            storageType, projectionType, templatesData, contents);
  }

  private PersistenceSetupTemplateData(final String basePackage,
                                       final String persistencePackage,
                                       final Boolean useCQRS,
                                       final StorageType storageType,
                                       final ProjectionType projectionType,
                                       final List<TemplateData> templatesData,
                                       final List<Content> contents) {
    this.templateParameters =
            loadParameters(basePackage, persistencePackage, useCQRS, storageType,
                    projectionType, templatesData, contents);
  }

  @SuppressWarnings("rawtypes")
  private TemplateParameters loadParameters(final String basePackage,
                                            final String persistencePackage,
                                            final Boolean useCQRS,
                                            final StorageType storageType,
                                            final ProjectionType projectionType,
                                            final List<TemplateData> templatesData,
                                            final List<Content> contents) {
    return TemplateParameters.with(TemplateParameter.BASE_PACKAGE, basePackage)
            .and(TemplateParameter.PACKAGE_NAME, persistencePackage)
            .and(TemplateParameter.STORAGE_TYPE, storageType.name())
            .and(TemplateParameter.DATA_OBJECTS, resolveDataObjectNames(contents))
            .and(TemplateParameter.USE_PROJECTIONS, projectionType.isProjectionEnabled())
            .and(TemplateParameter.PROJECTION_TYPE, projectionType.name())
            .and(TemplateParameter.ADAPTERS, Adapter.from(templatesData))
            .and(TemplateParameter.PROJECTIONS, Projection.from(contents))
            .and(TemplateParameter.QUERIES, Queries.from(useCQRS, contents, templatesData))
            .and(TemplateParameter.USE_CQRS, useCQRS).and(TemplateParameter.USE_ANNOTATIONS, true)
            .addImports(resolveImports(useCQRS, contents))
            .andResolve(TemplateParameter.REQUIRE_ADAPTERS, params -> !params.<List>find(TemplateParameter.ADAPTERS).isEmpty());
  }

  private Set<String> resolveImports(final boolean useCQRS, final List<Content> contents) {
    if (useCQRS) {
      return ContentQuery.findFullyQualifiedClassNames(contents, JavaTemplateStandard.AGGREGATE_STATE, JavaTemplateStandard.DATA_OBJECT, JavaTemplateStandard.DOMAIN_EVENT);
    }
    return ContentQuery.findFullyQualifiedClassNames(contents, JavaTemplateStandard.AGGREGATE_STATE, JavaTemplateStandard.DATA_OBJECT);
  }

  private String resolveDataObjectNames(final List<Content> contents) {
    return ContentQuery.findClassNames(JavaTemplateStandard.DATA_OBJECT, contents).stream()
            .map(name -> name + ".class").collect(Collectors.joining(", "));
  }

  @Override
  public TemplateParameters parameters() {
    return templateParameters;
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.PERSISTENCE_SETUP;
  }

}
