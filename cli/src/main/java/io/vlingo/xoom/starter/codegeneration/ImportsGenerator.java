// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import java.util.ArrayList;
import java.util.List;

public abstract class ImportsGenerator extends BaseGenerator {
  protected final List<ImportHolder> importsHolder;

  public ImportsGenerator() {
    super();

    this.importsHolder = new ArrayList<>();
    this.input.put("imports", importsHolder);
  }

  public void inputOf(final ImportHolder importHolder) {
    importsHolder.add(importHolder);
  }
}
