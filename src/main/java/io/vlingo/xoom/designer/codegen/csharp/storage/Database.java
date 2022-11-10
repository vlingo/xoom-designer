package io.vlingo.xoom.designer.codegen.csharp.storage;

public class Database {

  private final Model model;
  private final String appName;
  private final DatabaseType databaseType;

  public Database(final String appName, final Model model, final DatabaseType databaseType) {
    this.model = model;
    this.appName = appName;
    this.databaseType = databaseType;
  }

  public String getType() {
    return databaseType.name().toUpperCase();
  }

  public String getName() {
    if (model.isCommandModel() || model.isQueryModel()) {
      return appName + "-" + model.name().toLowerCase();
    }
    return appName;
  }

  public String getUrl() {
    return databaseType.connectionUrl;
  }

  public String getDriver() {
    return databaseType.driver;
  }

  public String getUsername() {
    return databaseType.username;
  }

  public String getPassword() {
    return databaseType.password;
  }

  public String getOriginator() {
    return databaseType.originator;
  }

}
