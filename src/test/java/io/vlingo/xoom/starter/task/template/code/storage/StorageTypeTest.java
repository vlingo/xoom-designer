// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.DatabaseType;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.DatabaseType.*;
import static io.vlingo.xoom.starter.task.template.code.storage.StorageType.STATE_STORE;

public class StorageTypeTest {

    @Test
    public void testParametersEnrichment() {
        final CodeTemplateParameters postgresParameters =
                STATE_STORE.enrichParameters(CodeTemplateParameters.with(DATABASE_TYPE, POSTGRES));

        Assertions.assertEquals("PostgresStorageDelegate", postgresParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate", postgresParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);

        final CodeTemplateParameters mySqlParameters =
                STATE_STORE.enrichParameters(CodeTemplateParameters.with(DATABASE_TYPE, MYSQL));

        Assertions.assertEquals("MySQLStorageDelegate", mySqlParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.mysql.MySQLStorageDelegate", mySqlParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);

        final CodeTemplateParameters hsqldbParameters =
                STATE_STORE.enrichParameters(CodeTemplateParameters.with(DATABASE_TYPE, HSQLDB));

        Assertions.assertEquals("HSQLDBStorageDelegate", hsqldbParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.hsqldb.HSQLDBStorageDelegate", hsqldbParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);

        final CodeTemplateParameters yugaByteParameters =
                STATE_STORE.enrichParameters(CodeTemplateParameters.with(DATABASE_TYPE, YUGA_BYTE));

        Assertions.assertEquals("YugaByteStorageDelegate", yugaByteParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.yugabyte.YugaByteStorageDelegate", yugaByteParameters.<List<ImportParameter>>find(IMPORTS).get(0).qualifiedClassName);
    }

    @Test
    public void testStoreActorQualifiedClassNameRetrieval() {
        Assertions.assertEquals("io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor",
                StorageType.qualifiedStoreActorNameFor(DatabaseType.IN_MEMORY, StorageType.JOURNAL));
        Assertions.assertEquals("io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor",
                StorageType.qualifiedStoreActorNameFor(DatabaseType.IN_MEMORY, StorageType.OBJECT_STORE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor",
                StorageType.qualifiedStoreActorNameFor(DatabaseType.IN_MEMORY, StorageType.STATE_STORE));

        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                StorageType.qualifiedStoreActorNameFor(POSTGRES, StorageType.STATE_STORE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                StorageType.qualifiedStoreActorNameFor(MYSQL, StorageType.STATE_STORE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                StorageType.qualifiedStoreActorNameFor(YUGA_BYTE, StorageType.STATE_STORE));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                StorageType.qualifiedStoreActorNameFor(HSQLDB, StorageType.STATE_STORE));
    }

}
