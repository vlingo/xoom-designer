// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure;

import io.vlingo.xoom.designer.codegen.InvalidResourcesPathException;
import io.vlingo.xoom.turbo.ComponentRegistry;

public class Infrastructure {

  public static void setupResources(final HomeDirectory homeDirectory,
                                    final Integer designerServerPort) {
    if (!homeDirectory.isValid()) {
      throw new InvalidResourcesPathException();
    }
    StagingFolder.resolve(homeDirectory);
    DesignerServerConfiguration.on(designerServerPort);
  }

  public static void clear() {
    ComponentRegistry.unregister(StagingFolder.class, DesignerServerConfiguration.class);
  }

}
