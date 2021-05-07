// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.unittest.entitty;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.storage.StorageType;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SourcedEvent {

  private final String name;
  private final String adapterName;

  public static List<SourcedEvent> from(final StorageType storageType,
                                        final CodeGenerationParameter aggregate) {
    if (!storageType.isSourced()) {
      return Collections.emptyList();
    }
    return aggregate.retrieveAllRelated(Label.DOMAIN_EVENT)
            .map(event -> new SourcedEvent(event.value))
            .collect(Collectors.toList());
  }

  private SourcedEvent(final String eventName) {
    this.name = eventName;
    this.adapterName = DesignerTemplateStandard.ADAPTER.resolveClassname(eventName);
  }

  public String getName() {
    return name;
  }

  public String getAdapterName() {
    return adapterName;
  }

}
