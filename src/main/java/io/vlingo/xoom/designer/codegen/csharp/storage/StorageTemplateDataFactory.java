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

import java.util.ArrayList;
import java.util.List;

public class StorageTemplateDataFactory {

  public static List<TemplateData> build(final String basePackage, final List<Content> contents, final StorageType storageType) {
    final String persistencePackage = PersistenceDetail.resolvePackage(basePackage);

    return new ArrayList<>(AdapterTemplateData.from(persistencePackage, storageType, contents));
  }

}
