// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

public class Database {

  private final Model model;
  private final String appName;
  private final DatabaseType databaseType;

  public Database(final String appName,
                  final Model model,
                  final DatabaseType databaseType) {
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
