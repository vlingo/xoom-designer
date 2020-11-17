// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import java.util.List;

public class GenerationSettingsData {

    public final ContextSettingsData context;
    public final ModelSettingsData model;
    public final DeploymentSettingsData deployment;
    public final String projectDirectory;
    public final Boolean useAnnotations;
    public final Boolean useAutoDispatch;

    public GenerationSettingsData(final ContextSettingsData context,
                                  final ModelSettingsData model,
                                  final DeploymentSettingsData deployment,
                                  final String projectDirectory,
                                  final Boolean useAnnotations,
                                  final Boolean useAutoDispatch) {
        this.context = context;
        this.model = model;
        this.deployment = deployment;
        this.projectDirectory = projectDirectory;
        this.useAnnotations = useAnnotations;
        this.useAutoDispatch = useAutoDispatch;
    }

    public List<String> validate(List<String> errorStrings) {
        if(context==null) {
            errorStrings.add("context is null");
        } else {
            context.validate(errorStrings);
        }
        if(model==null) {
            errorStrings.add("model is null");
        } else {
            model.validate(errorStrings);
        }
        if(deployment==null) {
            errorStrings.add("deployment is null");
        } else {
            deployment.validate(errorStrings);
        }
        if(projectDirectory==null) errorStrings.add("projectDirectory is null");
        if(useAnnotations==null) errorStrings.add("useAnnotations is null");
        if(useAutoDispatch==null) errorStrings.add("useAutoDispatch is null");
        return errorStrings;
    }
}
