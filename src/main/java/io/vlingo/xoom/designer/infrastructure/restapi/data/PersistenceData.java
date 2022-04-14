// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.List;

public class PersistenceData {

    public final String storageType;
    public final Boolean useCQRS;
    public final String projections;
    public final String database;
    public final String commandModelDatabase;
    public final String queryModelDatabase;

    public PersistenceData(final String storageType,
                           final Boolean useCQRS,
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

    public List<String> validate(List<String> errorStrings) {
        if(storageType==null) errorStrings.add("PersistenceData.storageType is null");
        if(useCQRS==null) errorStrings.add("PersistenceData.useCQRS is null");
        if(projections==null) errorStrings.add("PersistenceData.projections is null");
        if(database==null && queryModelDatabase==null && commandModelDatabase==null) errorStrings.add("PersistenceData.database is null"); 
        if(commandModelDatabase==null && database==null) errorStrings.add("PersistenceData.commandModelDatabase is null");
        if(queryModelDatabase==null && database==null) errorStrings.add("PersistenceData.queryModelDatabase is null");
        return errorStrings;
    }
}