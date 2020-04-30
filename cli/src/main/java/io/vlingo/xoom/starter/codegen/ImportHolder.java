// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

public class ImportHolder {
  private final String fullyQualifiedClassName;

  public ImportHolder(final String className) {
    this.fullyQualifiedClassName = className;
  }

  public String getFullyQualifiedClassName() {
    return fullyQualifiedClassName;
  }
}
