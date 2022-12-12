package io.vlingo.xoom.designer.codegen.csharp.storage;

public enum DatabaseType {

  IN_MEMORY("in_memory");
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
