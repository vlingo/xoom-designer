// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import java.util.List;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.DOMAIN_EVENT;
import static java.util.stream.Collectors.toList;

public class SourcedEvent {

  private final String entityName;
  private final String domainEventName;

  public static List<SourcedEvent> from(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(DOMAIN_EVENT)
            .map(event -> new SourcedEvent(aggregate, event))
            .collect(toList());
  }

  private SourcedEvent(final CodeGenerationParameter aggregate,
                       final CodeGenerationParameter event) {
    this.domainEventName = event.value;
    this.entityName = AGGREGATE.resolveClassname(aggregate.value);
  }

  public String getEntityName() {
    return entityName;
  }

  public String getDomainEventName() {
    return domainEventName;
  }

}