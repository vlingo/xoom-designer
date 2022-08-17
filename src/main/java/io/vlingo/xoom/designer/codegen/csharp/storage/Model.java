// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.storage;

import java.util.stream.Stream;

public enum Model {

  DOMAIN("DomainModel"),
  COMMAND("CommandModel"),
  QUERY("QueryModel");

  public final String title;

  Model(String title) {
    this.title = title;
  }

  public static Stream<Model> applicableToQueryAndCommand() {
    return Stream.of(QUERY, COMMAND);
  }

  public static Stream<Model> applicableToDomain() {
    return Stream.of(DOMAIN);
  }

  public boolean isCommandModel() {
    return equals(COMMAND);
  }

  public boolean isQueryModel() {
    return equals(QUERY);
  }

  public boolean isDomainModel() {
    return equals(DOMAIN);
  }
}
