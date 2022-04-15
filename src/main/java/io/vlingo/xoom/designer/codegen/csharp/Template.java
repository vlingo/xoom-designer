// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

public enum Template {
  SOLUTION_SETTINGS("Solution"),
  PROJECT_SETTINGS("Project"),
  ACTOR_SETTINGS("ActorSettings");

  public final String filename;

  Template(final String filename) {
    this.filename = filename;
  }

}
