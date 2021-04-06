// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.gui;

import io.vlingo.actors.Grid;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.StaticFilesConfiguration;
import io.vlingo.xoom.XoomInitializationAware;
import io.vlingo.xoom.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.annotation.initializer.Xoom;
import io.vlingo.xoom.starter.infrastructure.Infrastructure;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Xoom(name = "xoom-starter")
@ResourceHandlers(packages = "io.vlingo.xoom.starter.restapi")
public class UserInterfaceBootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

  @Override
  public Configuration configureServer(final Grid grid, final String[] args) {
    final URL serverUrlPath = Infrastructure.StarterServer.url();
    return Configuration.define().withPort(serverUrlPath.getPort());
  }

  @Override
  public StaticFilesConfiguration staticFilesConfiguration() {
    final List<String> subPaths = Arrays.asList("/", "/client", "/aggregates", "/context", "/deployment", "/generation", "/persistence");
    return StaticFilesConfiguration.defineWith(100, "frontend", subPaths);
  }

}
