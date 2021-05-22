// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.ImportParameter;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;

import java.util.List;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.TemplateParameter.*;

public class AdapterTemplateData extends TemplateData {

  private final String sourceClassName;
  private final TemplateStandard sourceClassStandard;
  private final TemplateParameters parameters;

  public static List<TemplateData> from(final String persistencePackage,
                                        final StorageType storageType,
                                        final List<Content> contents) {
    return ContentQuery.findClassNames(storageType.adapterSourceClassStandard, contents)
            .stream().map(sourceClassName ->
                    new AdapterTemplateData(sourceClassName,
                            storageType.adapterSourceClassStandard,
                            persistencePackage, storageType, contents)
            ).collect(Collectors.toList());
  }

  public AdapterTemplateData(final String sourceClassName,
                             final TemplateStandard sourceClassStandard,
                             final String persistencePackage,
                             final StorageType storageType,
                             final List<Content> contents) {
    this.sourceClassName = sourceClassName;
    this.sourceClassStandard = sourceClassStandard;
    this.parameters = loadParameters(persistencePackage, storageType, contents);
  }

  private TemplateParameters loadParameters(final String packageName,
                                            final StorageType storageType,
                                            final List<Content> contents) {
    final String sourceQualifiedClassName =
            ContentQuery.findFullyQualifiedClassName(sourceClassStandard, sourceClassName, contents);

    return TemplateParameters.with(PACKAGE_NAME, packageName)
            .and(IMPORTS, ImportParameter.of(sourceQualifiedClassName))
            .and(SOURCE_NAME, sourceClassName)
            .and(ADAPTER_NAME, JavaTemplateStandard.ADAPTER.resolveClassname(sourceClassName))
            .and(STORAGE_TYPE, storageType);
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public String filename() {
    return standard().resolveFilename(sourceClassName, parameters);
  }

  @Override
  public TemplateStandard standard() {
    return JavaTemplateStandard.ADAPTER;
  }

}
