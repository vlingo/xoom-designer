// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.csharp.PersistenceDetail;
import io.vlingo.xoom.designer.codegen.csharp.projections.ProjectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StorageTemplateDataFactory {

  public static List<TemplateData> build(final String basePackage, final List<Content> contents,
                                         final StorageType storageType, final ProjectionType projectionType) {
    final String persistencePackage = PersistenceDetail.resolvePackage(basePackage);

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.addAll(AdapterTemplateData.from(persistencePackage, storageType, contents));
    templatesData.addAll(buildStoreProvidersTemplateData(persistencePackage, storageType, projectionType,
        templatesData, contents, Model.applicableToDomain()));
    return templatesData;
  }

  public static List<TemplateData> buildWithCqrs(final String basePackage, final List<Content> contents,
                                                 final StorageType storageType, final ProjectionType projectionType) {
    final String persistencePackage = PersistenceDetail.resolvePackage(basePackage);

    final List<TemplateData> templatesData = new ArrayList<>();
    templatesData.addAll(AdapterTemplateData.from(persistencePackage, storageType, contents));
    templatesData.addAll(QueriesTemplateDataFactory.from(persistencePackage, contents));
    templatesData.addAll(buildStoreProvidersTemplateData(persistencePackage, storageType, projectionType,
        templatesData, contents, Model.applicableToQueryAndCommand()));
    return templatesData;
  }

  private static List<TemplateData> buildStoreProvidersTemplateData(final String persistencePackage,
                                                                    final StorageType storageType,
                                                                    final ProjectionType projectionType,
                                                                    final List<TemplateData> templatesData,
                                                                    final List<Content> contents,
                                                                    final Stream<Model> models) {
    return StorageProviderTemplateData.from(persistencePackage, storageType, projectionType,
        templatesData, contents, models);
  }
}
