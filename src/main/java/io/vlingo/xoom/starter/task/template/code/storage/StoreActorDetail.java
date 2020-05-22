// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.DatabaseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreActorDetail {

    private final StorageType storageType;
    public final String storeActorQualifiedName;
    private final DatabaseType databaseType;

    public StoreActorDetail(final DatabaseType databaseType,
                               final StorageType storageType,
                               final String storeActorQualifiedName) {
        this.databaseType = databaseType;
        this.storageType = storageType;
        this.storeActorQualifiedName = storeActorQualifiedName;
    }

    public boolean relateTo(final DatabaseType databaseType, final StorageType storageType) {
        return this.databaseType.equals(databaseType) && this.storageType.equals(storageType);
    }

}
