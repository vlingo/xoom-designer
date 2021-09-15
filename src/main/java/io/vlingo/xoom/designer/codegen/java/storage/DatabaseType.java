// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

public enum DatabaseType {

  IN_MEMORY("in_memory"),
  POSTGRES("postgres", "org.postgresql.Driver", "jdbc:postgresql://localhost/", "postgres", "", "main"),
  HSQLDB("hsqldb", "org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:mem:", "sa", "", "main"),
  MYSQL("mysql", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost/", "root", "", "main"),
  YUGA_BYTE("yugabyte", "org.postgresql.Driver", "jdbc:postgresql://localhost/", "cassandra", "cassandra", "main");

  public final String label;
  public final String driver;
  public final String connectionUrl;
  public final String username;
  public final String password;
  public final String originator;
  public final boolean configurable;

  DatabaseType(final String label) {
    this(label, "", "", "", "", "", false);
  }

  DatabaseType(final String label,
               final String driver,
               final String connectionUrl,
               final String username,
               final String password,
               final String originator) {
    this(label, driver, connectionUrl, username, password, originator, true);
  }

  DatabaseType(final String label,
               final String driver,
               final String connectionUrl,
               final String username,
               final String password,
               final String originator,
               final boolean configurable) {
    this.label = label;
    this.driver = driver;
    this.connectionUrl = connectionUrl;
    this.username = username;
    this.password = password;
    this.originator = originator;
    this.configurable = configurable;
  }

  public static DatabaseType getOrDefault(final String name, final DatabaseType defaultDatabase) {
    if (name == null || name.trim().isEmpty()) {
      return defaultDatabase;
    }
    return valueOf(name);
  }

}
