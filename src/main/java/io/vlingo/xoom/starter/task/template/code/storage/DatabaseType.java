// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code.storage;

import io.vlingo.xoom.starter.Configuration;
import io.vlingo.xoom.starter.task.template.code.CodeTemplateParameters;
import io.vlingo.xoom.starter.task.template.code.storage.ModelClassification;
import io.vlingo.xoom.starter.task.template.code.storage.StorageType;

import static io.vlingo.xoom.starter.Configuration.STORAGE_DELEGATE_QUALIFIED_NAME_PATTERN;
import static io.vlingo.xoom.starter.task.template.code.CodeTemplateParameter.*;

public enum DatabaseType {

    IN_MEMORY,
    POSTGRES("postgres", "jdbc:postgresql://localhost", "io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider"),
    HSQLDB("hsqldb", "jdbc:hsqldb:mem:", "io.vlingo.symbio.store.common.jdbc.hsqldb.HSQLDBConfigurationProvider"),
    MYSQL("mysql", "jdbc:mysql://localhost/", "io.vlingo.symbio.store.common.jdbc.mysql.MySQLConfigurationProvider"),
    YUGA_BYTE("yugabyte", "jdbc:postgresql://localhost", "io.vlingo.symbio.store.common.jdbc.yugabyte.YugaByteConfigurationProvider");

    public final String label;
    public final String connectionUrl;
    public final String configurationProviderQualifiedName;
    public final boolean configurable;

    DatabaseType() {
        this(null, null, null, false);
    }

    DatabaseType(final String label,
                 final String connectionUrl,
                 final String configurationProviderQualifiedName) {
        this(label, connectionUrl, configurationProviderQualifiedName, true);
    }

    DatabaseType(final String label,
                 final String connectionUrl,
                 final String configurationProviderQualifiedName,
                 final boolean configurable) {
        this.label = label;
        this.connectionUrl = connectionUrl;
        this.configurationProviderQualifiedName = configurationProviderQualifiedName;
        this.configurable = configurable;
    }

    public String configurationProviderName() {
        final int classNameIndex = configurationProviderQualifiedName.lastIndexOf(".");
        return configurationProviderQualifiedName.substring(classNameIndex + 1);
    }

    public CodeTemplateParameters addConfigurationParameters(final CodeTemplateParameters parameters) {
        if(!configurable) {
            return parameters;
        }

        final StorageType storageType = parameters.find(STORAGE_TYPE);
        final ModelClassification modelClassification = parameters.find(MODEL_CLASSIFICATION);

        if(modelClassification.isQueryModel() || storageType.isStateful()) {

            final String storageDelegateClassName =
                    Configuration.STORAGE_DELEGATE_CLASS_NAME.get(this);

            final String storageDelegateQualifiedClassName =
                    String.format(STORAGE_DELEGATE_QUALIFIED_NAME_PATTERN,
                            label, storageDelegateClassName);

            parameters.and(STORAGE_DELEGATE_NAME, storageDelegateClassName)
                    .addImport(storageDelegateQualifiedClassName);
        }

        return parameters.and(CONFIGURATION_PROVIDER_NAME, this.configurationProviderName())
                .addImport(configurationProviderQualifiedName);
    }

    public boolean isInMemory() {
        return equals(IN_MEMORY);
    }
}
