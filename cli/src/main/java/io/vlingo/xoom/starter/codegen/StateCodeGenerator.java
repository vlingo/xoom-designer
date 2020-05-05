// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import io.vlingo.xoom.starter.task.template.StorageType;

import static io.vlingo.xoom.starter.ApplicationConfiguration.STATE_TEMPLATES;

public class StateCodeGenerator extends BaseGenerator {

  private static StateCodeGenerator instance;

  private StateCodeGenerator() {
  }

  public String generate(final String stateName,
                         final String packageName,
                         final StorageType storageType) {
    this.input("stateClass", stateName);
    this.input("packageName", packageName);
    this.template = STATE_TEMPLATES.get(storageType);
    return generate();
  }

  public static StateCodeGenerator instance() {
    if(instance == null){
      instance = new StateCodeGenerator();
    }
    return instance;
  }

}
