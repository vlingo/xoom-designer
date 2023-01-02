// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.designer.codegen.GenerationTarget;
import io.vlingo.xoom.designer.infrastructure.BrowserLauncher;
import io.vlingo.xoom.designer.infrastructure.HomeDirectory;
import io.vlingo.xoom.designer.infrastructure.Infrastructure;
import io.vlingo.xoom.terminal.CommandExecutionProcess;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Map;

public class DesignerInitializer {

  private final BrowserLauncher browserLauncher;

  public DesignerInitializer(final CommandExecutionProcess commandExecutionProcess) {
    this.browserLauncher = new BrowserLauncher(commandExecutionProcess);
  }

  public void start(final Map<String, String> options) {
    registerProfile(options);
    initializeResources(options);
    registerGenerationTarget(options);
    startServer();
    launchBrowser();
  }

  private void registerProfile(final Map<String, String> options) {
    final String profileName =
            options.get(Profile.optionName());

    if (profileName.equalsIgnoreCase(Profile.TEST.name())) {
      ComponentRegistry.withType(Logger.class).info("Enabling Test profile");
      Profile.enableTestProfile();
    }
  }

  private void initializeResources(final Map<String, String> options) {
    final HomeDirectory homeDirectory = HomeDirectory.fromEnvironment();
    final Integer designerServerPort = Integer.valueOf(options.get("port"));
    Infrastructure.setupResources(homeDirectory, designerServerPort);
    Configuration.load();
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
    if(Configuration.resolveEnvironment().isLocal() && !Profile.isTestProfileEnabled()) {
      this.browserLauncher.execute();
    }
  }

}
