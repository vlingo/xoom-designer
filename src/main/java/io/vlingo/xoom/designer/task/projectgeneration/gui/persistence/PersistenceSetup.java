// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui.persistence;

import io.vlingo.xoom.designer.task.projectgeneration.gui.request.RequestHistoryPreserved;
import io.vlingo.xoom.designer.task.projectgeneration.gui.request.RequestHistoryState;
import io.vlingo.xoom.turbo.annotation.persistence.*;

import static io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType.STATE_STORE;

@Persistence(basePackage = "io.vlingo.xoom.designer.task.projectgeneration.gui", storageType = STATE_STORE, cqrs = true)
@Projections(type = ProjectionType.OPERATION_BASED, value = {
  @Projection(actor = TotalRequestsByIPProjectionActor.class, becauseOf = RequestHistoryPreserved.class)
})
@Adapters(RequestHistoryState.class)
@DataObjects({TotalRequestsByIPData.class})
public class PersistenceSetup {

}
