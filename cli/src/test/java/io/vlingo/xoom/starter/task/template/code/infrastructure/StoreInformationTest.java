// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.template.StorageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StoreInformationTest {

    @Test
    public void testInMemoryStoreQualifiedClassNameRetrieval() {
        Assertions.assertEquals("io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor",
                StoreInformation.qualifiedNameFor(DatabaseType.IN_MEMORY, StorageType.JOURNAL));
        Assertions.assertEquals("io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor",
                StoreInformation.qualifiedNameFor(DatabaseType.IN_MEMORY, StorageType.OBJECT_STORE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor",
                StoreInformation.qualifiedNameFor(DatabaseType.IN_MEMORY, StorageType.STATE_STORE));
    }

}
