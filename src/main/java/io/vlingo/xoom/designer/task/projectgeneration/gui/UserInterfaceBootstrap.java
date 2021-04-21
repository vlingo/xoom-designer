// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.StaticFilesConfiguration;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "xoom-designer")
@ResourceHandlers(packages = "io.vlingo.xoom.designer.restapi")
public class UserInterfaceBootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

  @Override
  public Configuration configureServer(final Grid grid, final String[] args) {
    final URL serverUrlPath = Infrastructure.DesignerServer.url();
    return Configuration.define().withPort(serverUrlPath.getPort());
  }

  @Override
  public StaticFilesConfiguration staticFilesConfiguration() {
    final List<String> subPaths = Arrays.asList("/", "/client", "/aggregates", "/context", "/deployment", "/generation", "/persistence");
    return StaticFilesConfiguration.defineWith(100, "frontend", subPaths);
  }

}
