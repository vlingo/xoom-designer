// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.steps.CommandResolverStep;

import static io.vlingo.xoom.starter.Resource.ARCHETYPES_FOLDER;

public class AngularSchematicExecutionStep extends CommandResolverStep {

  @Override
  protected String formatCommands(final TaskExecutionContext context) {
    final String schematicCommand = "ng schematic";
    final String temporaryProjectPath = context.temporaryProjectPath();
    final String projectFolderCommand = resolveDirectoryChangeCommand(temporaryProjectPath);
    return projectFolderCommand + " && " + schematicCommand;
  }

}
