// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegen;

import java.util.ArrayList;
import java.util.List;

public class StateStoreProviderGenerator extends ImportsGenerator {
  protected final List<StateAdapterHolder> stateAdaptersHolder;

  public StateStoreProviderGenerator() {
    super();

    this.stateAdaptersHolder = new ArrayList<>();
    input.put("stateAdapters", stateAdaptersHolder);
  }

  public void inputOf(final StateAdapterHolder stateAdapterHolder) {
    stateAdaptersHolder.add(stateAdapterHolder);
  }

  public void inputOfStateStoreClassName(final String stateStoreClassName) {
    input.put("stateStoreClassName", stateStoreClassName);
  }
}
