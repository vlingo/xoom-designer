// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.infrastructure;

import io.vlingo.xoom.starter.DatabaseType;
import io.vlingo.xoom.starter.task.template.StorageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoreInformation {

    private final StorageType storageType;
    public final String storeActorQualifiedName;
    private final List<String> initializationParameters = new ArrayList<>();
    private final DatabaseType databaseType;
    //TODO: Include StorageDelegate class here when supported

    private StoreInformation(final DatabaseType databaseType,
                             final StorageType storageType,
                             final String storeActorQualifiedName,
                             final String ...initializationParameters) {
        this.databaseType = databaseType;
        this.storageType = storageType;
        this.storeActorQualifiedName = storeActorQualifiedName;
        this.initializationParameters.addAll(Arrays.asList(initializationParameters));
    }

    public static String qualifiedNameFor(final DatabaseType databaseType, final StorageType storageType) {
        return SUPPORTED_STORES.stream()
                .filter(information -> information.relateTo(databaseType, storageType))
                .findFirst().orElseThrow(() -> new StoreInformationNotFoundException(databaseType, storageType))
                .storeActorQualifiedName;
    }

    private boolean relateTo(final DatabaseType databaseType, final StorageType storageType) {
        return this.databaseType.equals(databaseType) && this.storageType.equals(storageType);
    }

    private static final List<StoreInformation> SUPPORTED_STORES =
            Arrays.asList(

                    //---  IN MEMORY ---//

                    new StoreInformation(
                            DatabaseType.IN_MEMORY,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor",
                            "InMemoryJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.IN_MEMORY,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor",
                            "InMemoryObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.IN_MEMORY,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor",
                            "InMemoryStateStoreActor.class"),

                    //--- POSTGRES ---//

                    new StoreInformation(
                            DatabaseType.POSTGRES,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
                            "JDBCJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.POSTGRES,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
                            "JDBCObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.POSTGRES,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                            "JDBCStateStoreActor.class"),

                    //--- HSQLDB ---//

                    new StoreInformation(
                            DatabaseType.HSQLDB,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
                            "JDBCJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.HSQLDB,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
                            "JDBCObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.HSQLDB,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                            "JDBCStateStoreActor.class"),

                    //--- MYSQL ---//

                    new StoreInformation(
                            DatabaseType.MYSQL,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
                            "JDBCJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.MYSQL,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
                            "JDBCObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.MYSQL,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                            "JDBCStateStoreActor.class"),

                    //--- YUGA_BYTE ---//

                    new StoreInformation(
                            DatabaseType.YUGA_BYTE,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
                            "JDBCJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.YUGA_BYTE,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
                            "JDBCObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.YUGA_BYTE,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                            "JDBCStateStoreActor.class"),

                    //--- MARIA_DB ---//

                    new StoreInformation(
                            DatabaseType.MARIA_DB,
                            StorageType.JOURNAL,
                            "io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor",
                            "JDBCJournalActor.class"),

                    new StoreInformation(
                            DatabaseType.MARIA_DB,
                            StorageType.OBJECT_STORE,
                            "io.vlingo.symbio.store.object.jdbc.JDBCObjectStoreActor",
                            "JDBCObjectStoreActor.class"),

                    new StoreInformation(
                            DatabaseType.MARIA_DB,
                            StorageType.STATE_STORE,
                            "io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor",
                            "JDBCStateStoreActor.class")
            );
}
