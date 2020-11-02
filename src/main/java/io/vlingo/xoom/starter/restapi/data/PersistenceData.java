// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

public class PersistenceData {

    public final String storageType;
    public final boolean useCQRS;
    public final String projections;
    public final String database;
    public final String commandModelDatabase;
    public final String queryModelDatabase;

    public PersistenceData(final String storageType,
                           final boolean useCQRS,
                           final String projections,
                           final String database,
                           final String commandModelDatabase,
                           final String queryModelDatabase) {
        this.storageType = storageType;
        this.useCQRS = useCQRS;
        this.projections = projections;
        this.database = database;
        this.commandModelDatabase = commandModelDatabase;
        this.queryModelDatabase = queryModelDatabase;
    }

}