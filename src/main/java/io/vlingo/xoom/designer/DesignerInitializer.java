// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.infrastructure.BrowserLauncher;
import io.vlingo.xoom.designer.infrastructure.DesignerServerConfiguration;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Map;

public class DesignerInitializer {

  private final BrowserLauncher browserLauncher;

  public DesignerInitializer(final CommandExecutionProcess browserLauncher) {
    this.browserLauncher = new BrowserLauncher(browserLauncher);
  }

  public void run(final Map<String, String> options) {
    registerProfile(options);
    initializeInfraResources(options);
    registerGenerationTarget(options);
    startServer();
    launchBrowser();
  }

  private void registerProfile(final Map<String, String> options) {
    final String profileName =
            options.get(Profile.class.getName().toUpperCase());

    if (profileName.equalsIgnoreCase(Profile.TEST.name())) {
      ComponentRegistry.withType(Logger.class).info("Enabling Test profile");
      Profile.enableTestProfile();
    }
  }

  private void initializeInfraResources(final Map<String, String> options) {
    final Integer designerServerPort = Integer.valueOf(options.get("port"));
    Infrastructure.setupResources(HomeDirectory.fromEnvironment(), designerServerPort);
  }

  private void registerGenerationTarget(final Map<String, String> options) {
    final String targetOption = options.get("target");
    ComponentRegistry.register(GenerationTarget.class, GenerationTarget.from(targetOption));
  }

  private void startServer() {
    try {
      XoomInitializer.main(new String[]{});
    } catch (final Exception exception) {
      throw new DesignerServerInitializationException(exception);
    }
  }

  private void launchBrowser() {
    if(ComponentsConfiguration.resolveEnvironment().isLocal() && !Profile.isTestProfileEnabled()) {
      this.browserLauncher.execute();
    }
  }

}
