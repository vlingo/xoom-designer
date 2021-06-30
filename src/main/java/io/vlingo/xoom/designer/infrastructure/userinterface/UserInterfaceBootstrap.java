// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.userinterface;

import io.vlingo.xoom.designer.Environment;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.DesignerServer;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestLimiter;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestLimiterFilter;
import io.vlingo.xoom.designer.infrastructure.requesthistory.RequestPreservationFilter;
import io.vlingo.xoom.http.Filters;
import io.vlingo.xoom.http.RequestFilter;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.StaticFilesConfiguration;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.vlingo.xoom.designer.Configuration.*;

@Xoom(name = "xoom-designer")
@ResourceHandlers(packages = "io.vlingo.xoom.designer.infrastructure.restapi")
public class UserInterfaceBootstrap implements XoomInitializationAware {

  private final Environment environment;

  public UserInterfaceBootstrap() {
    environment = resolveEnvironment();
  }

  @Override
  public void onInit(final Grid grid) {
    if (environment.isCloud()) {
      Settings.clear();
    }
  }

  @Override
  public Configuration configureServer(final Grid grid, final String[] args) {
    return Configuration.define().withPort(DesignerServer.url().getPort())
            .with(Filters.are(requestFilters(grid), Filters.noResponseFilters()));
  }

  @Override
  public StaticFilesConfiguration staticFilesConfiguration() {
    final List<String> subPaths = Arrays.asList("/", "/client", "/aggregates", "/context", "/deployment", "/generation", "/persistence");
    return StaticFilesConfiguration.defineWith(100, "frontend", subPaths);
  }

  private List<RequestFilter> requestFilters(final Grid grid) {
    if(!environment.isCloud()) {
        return Collections.emptyList();
    }

    final RequestLimiter requestLimiter =
            RequestLimiter.of(resolveProjectGenerationRequestLimit(),
                    resolveProjectGenerationRequestCountExpiration());

    return Arrays.asList(RequestLimiterFilter.with(requestLimiter), RequestPreservationFilter.with(grid.world().stage()));
  }
}
