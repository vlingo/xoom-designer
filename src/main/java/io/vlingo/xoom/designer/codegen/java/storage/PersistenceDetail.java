// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.storage;

public class PersistenceDetail {

  private final static String PACKAGE_PATTERN = "%s.%s.%s";
  private final static String PARENT_PACKAGE_NAME = "infrastructure";
  private final static String PERSISTENCE_PACKAGE_NAME = "persistence";

  public static String resolvePackage(final String basePackage) {
    return String.format(PACKAGE_PATTERN, basePackage, PARENT_PACKAGE_NAME, PERSISTENCE_PACKAGE_NAME).toLowerCase();
  }
}
