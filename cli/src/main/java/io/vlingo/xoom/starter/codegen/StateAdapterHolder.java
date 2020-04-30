// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

public class StateAdapterHolder {
  private final String stateAdapterClass;
  private final String stateClass;

  public StateAdapterHolder(final String stateClass) {
    this.stateClass = stateClass;
    this.stateAdapterClass = stateClass + "Adapter";
  }

  public String getStateAdapterClass() {
    return stateAdapterClass;
  }

  public String getStateClass() {
    return stateClass;
  }
}
