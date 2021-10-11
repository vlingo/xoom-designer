// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cli.option;

public enum OptionName {

  TAG("tag"),
  TARGET("target"),
  PORT("port"),
  PROFILE("profile"),
  CURRENT_DIRECTORY("currentDirectory"),
  PUBLISHER("publisher");

  private final String value;

  OptionName(final String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public String withPrefix() {
    return Option.prefix + value;
  }

}
