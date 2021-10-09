// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure;

public class ApplicationDirectory {

  public final String path;

  public static ApplicationDirectory from(final String path) {
    return new ApplicationDirectory(path);
  }

  public ApplicationDirectory(final String path) {
    this.path = path;
  }

}