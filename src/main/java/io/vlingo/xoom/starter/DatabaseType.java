// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter;

public enum DatabaseType {

    IN_MEMORY,
    POSTGRES("", ""),
    HSQLDB("", ""),
    MYSQL("", ""),
    YUGA_BYTE("", ""),
    MARIA_DB("", "");

    public final boolean requiresStorageDelegate;
    public final String connectionUrl;
    public final String driverClass;

    DatabaseType() {
        this(null, null, false);
    }

    DatabaseType(final String connectionUrl,
                 final String driverClass) {
        this(connectionUrl, driverClass, true);
    }

    DatabaseType(final String connectionUrl,
                 final String driverClass,
                 final boolean requiresStorageDelegate) {
        this.connectionUrl = connectionUrl;
        this.driverClass = driverClass;
        this.requiresStorageDelegate = requiresStorageDelegate;
    }

}
