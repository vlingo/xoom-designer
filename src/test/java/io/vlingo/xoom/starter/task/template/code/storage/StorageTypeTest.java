// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.template.code.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.vlingo.xoom.starter.task.template.code.storage.DatabaseType.*;
import static io.vlingo.xoom.starter.task.template.code.storage.DatabaseType.HSQLDB;
import static io.vlingo.xoom.starter.task.template.code.storage.StorageType.*;
import static io.vlingo.xoom.starter.task.template.code.storage.StorageType.STATE_STORE;

public class StorageTypeTest {

    @Test
    public void testStoreActorQualifiedClassNameRetrieval() {
        Assertions.assertEquals("io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor",
                JOURNAL.actorFor(DatabaseType.IN_MEMORY));
        Assertions.assertEquals("io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor",
                OBJECT_STORE.actorFor(DatabaseType.IN_MEMORY));
        Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor",
                STATE_STORE.actorFor(DatabaseType.IN_MEMORY));

        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                STATE_STORE.actorFor(POSTGRES));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                STATE_STORE.actorFor(MYSQL));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                STATE_STORE.actorFor(YUGA_BYTE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                STATE_STORE.actorFor(HSQLDB));
    }

}
