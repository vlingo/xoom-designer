// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.gui;

import io.vlingo.xoom.designer.Environment;
import io.vlingo.xoom.designer.infrastructure.Infrastructure.DesignerServer;
import io.vlingo.xoom.designer.task.projectgeneration.gui.request.RequestLimiter;
import io.vlingo.xoom.designer.task.projectgeneration.gui.request.RequestLimiterFilter;
import io.vlingo.xoom.designer.task.projectgeneration.gui.request.UsageData;
import io.vlingo.xoom.designer.task.projectgeneration.gui.request.UsageDataFilter;
import io.vlingo.xoom.http.Filters;
import io.vlingo.xoom.http.RequestFilter;
import io.vlingo.xoom.http.resource.Configuration;
import io.vlingo.xoom.http.resource.StaticFilesConfiguration;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.ResourceHandlers;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.vlingo.xoom.designer.Configuration.*;
import static io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType.STATE_STORE;
import static io.vlingo.xoom.turbo.storage.Model.QUERY;
import static java.util.Collections.emptyList;

@Xoom(name = "xoom-designer")
@ResourceHandlers(packages = "io.vlingo.xoom.designer.task.projectgeneration.restapi")
public class UserInterfaceBootstrap implements XoomInitializationAware {

  private StateStore stateStore;
  private final Environment environment;

  public UserInterfaceBootstrap() {
    environment = resolveEnvironment();
  }

  @Override
  public void onInit(final Grid grid) {
    if (environment.isCloud()) {
      StateTypeStateStoreMap.stateTypeToStoreName(UsageData.class, UsageData.class.getSimpleName());
      stateStore = StoreActorBuilder.from(grid.world().stage(), QUERY, emptyList(), STATE_STORE, new Properties(), true);
    }
  }

  @Override
  public Configuration configureServer(final Grid grid, final String[] args) {
    final RequestLimiter requestLimiter =
            RequestLimiter.of(resolveProjectGenerationRequestLimit(),
                    resolveProjectGenerationRequestCountExpiration());

    final List<RequestFilter> requestFilters =
            Arrays.asList(RequestLimiterFilter.with(requestLimiter),
                    UsageDataFilter.with(stateStore, grid.world().defaultLogger()));

    return Configuration.define().withPort(DesignerServer.url().getPort())
            .with(Filters.are(requestFilters, Filters.noResponseFilters()));
  }

  @Override
  public StaticFilesConfiguration staticFilesConfiguration() {
    final List<String> subPaths = Arrays.asList("/", "/client", "/aggregates", "/context", "/deployment", "/generation", "/persistence");
    return StaticFilesConfiguration.defineWith(100, "frontend", subPaths);
  }

}
