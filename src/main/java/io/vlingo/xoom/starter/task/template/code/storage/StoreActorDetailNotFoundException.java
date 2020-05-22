// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.DatabaseType;
import io.vlingo.xoom.starter.task.TaskExecutionException;

public class StoreActorDetailNotFoundException extends TaskExecutionException {

    public StoreActorDetailNotFoundException(final DatabaseType databaseType, final StorageType storageType) {
        super("Unable to find Store Information for " + databaseType + " supporting " + storageType);
    }

}
