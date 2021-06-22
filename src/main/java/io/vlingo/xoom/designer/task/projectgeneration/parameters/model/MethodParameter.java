// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.model;

import java.util.List;

public class MethodParameter {

  private final String name;
  private final Field stateField;
  private final DomainEvent event;
  private final CollectionMutation collectionMutation;

  public MethodParameter(final String name,
                         final Field stateField,
                         final DomainEvent event,
                         final CollectionMutation collectionMutation) {
    this.name = name;
    this.stateField = stateField;
    this.collectionMutation = collectionMutation;
    this.event = event;
  }

  public List<String> resolveCollectionMutationStatements(final String collectionOwner) {
    return collectionMutation.resolveStatements(collectionOwner, this);
  }

  public String name() {
    return name;
  }

  public String stateFieldName() {
    return stateField.name();
  }

}
