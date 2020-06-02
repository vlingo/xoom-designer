// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.ImportParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;
import static io.vlingo.xoom.starter.task.template.code.storage.DatabaseType.*;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.COMMAND;
import static io.vlingo.xoom.starter.task.template.code.storage.ModelClassification.QUERY;
import static io.vlingo.xoom.starter.task.template.code.storage.StorageType.*;

public class DatabaseTypeTest {

    @Test
    public void testParametersEnrichmentOnProductionDatabases() {

        final CodeTemplateParameters postgresParametersOnQueryModel =
                POSTGRES.addConfigurationParameters(CodeTemplateParameters.with(STORAGE_TYPE, STATE_STORE)
                        .and(MODEL_CLASSIFICATION, QUERY));

        Assertions.assertEquals("PostgresStorageDelegate", postgresParametersOnQueryModel.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate", postgresParametersOnQueryModel.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider", postgresParametersOnQueryModel.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());

        final CodeTemplateParameters postgresParametersOnCommandModel =
                POSTGRES.addConfigurationParameters(CodeTemplateParameters.with(STORAGE_TYPE, STATE_STORE)
                        .and(MODEL_CLASSIFICATION, COMMAND));

        Assertions.assertEquals("PostgresStorageDelegate", postgresParametersOnCommandModel.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate", postgresParametersOnCommandModel.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider", postgresParametersOnQueryModel.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());

        final CodeTemplateParameters mySqlParameters =
                MYSQL.addConfigurationParameters(CodeTemplateParameters.with(STORAGE_TYPE, STATE_STORE)
                        .and(MODEL_CLASSIFICATION, QUERY));

        Assertions.assertEquals("MySQLStorageDelegate", mySqlParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.mysql.MySQLStorageDelegate", mySqlParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.mysql.MySQLConfigurationProvider", mySqlParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());

        final CodeTemplateParameters hsqldbParameters =
                HSQLDB.addConfigurationParameters(CodeTemplateParameters.with(STORAGE_TYPE, STATE_STORE)
                        .and(MODEL_CLASSIFICATION, QUERY));

        Assertions.assertEquals("HSQLDBStorageDelegate", hsqldbParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.hsqldb.HSQLDBStorageDelegate", hsqldbParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.hsqldb.HSQLDBConfigurationProvider", hsqldbParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());

        final CodeTemplateParameters yugaByteParameters =
                YUGA_BYTE.addConfigurationParameters(CodeTemplateParameters.with(STORAGE_TYPE, STATE_STORE)
                        .and(MODEL_CLASSIFICATION, QUERY));

        Assertions.assertEquals("YugaByteStorageDelegate", yugaByteParameters.find(STORAGE_DELEGATE_NAME));
        Assertions.assertEquals("io.vlingo.symbio.store.state.jdbc.yugabyte.YugaByteStorageDelegate", yugaByteParameters.<List<ImportParameter>>find(IMPORTS).get(0).getQualifiedClassName());
        Assertions.assertEquals("io.vlingo.symbio.store.common.jdbc.yugabyte.YugaByteConfigurationProvider", yugaByteParameters.<List<ImportParameter>>find(IMPORTS).get(1).getQualifiedClassName());
    }

}
