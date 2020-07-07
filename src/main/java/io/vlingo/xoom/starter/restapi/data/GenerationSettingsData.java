// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.template.Agent;

import java.util.ArrayList;
import java.util.List;

import static io.vlingo.xoom.starter.task.Property.ANNOTATIONS;
import static io.vlingo.xoom.starter.task.Property.TARGET_FOLDER;
import static io.vlingo.xoom.starter.task.Task.TEMPLATE_GENERATION;

public class GenerationSettingsData {

    public final ContextSettingsData context;
    public final ModelSettingsData model;
    public final DeploymentSettingsData deployment;
    public final String projectDirectory;
    public final Boolean annotations;

    public GenerationSettingsData(final ContextSettingsData context,
                                  final ModelSettingsData model,
                                  final DeploymentSettingsData deployment,
                                  final String projectDirectory,
                                  final Boolean annotations) {
        this.context = context;
        this.model = model;
        this.deployment = deployment;
        this.projectDirectory = projectDirectory;
        this.annotations = annotations;
    }

    public List<String> toArguments() {
        final List<String> args = new ArrayList<>();
        args.add(TEMPLATE_GENERATION.command());
        args.addAll(context.toArguments());
        args.addAll(model.toArguments());
        args.addAll(deployment.toArguments());
        args.add(TARGET_FOLDER.literal());
        args.add(projectDirectory);
        args.add(ANNOTATIONS.literal());
        args.add(annotations.toString());
        args.add(Agent.argumentKey());
        args.add(Agent.WEB.name());
        return args;
    }
}
