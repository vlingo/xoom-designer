// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.storage.DatabaseType;

import java.util.ArrayList;
import java.util.List;

public class DockerComposeTemplateData extends TemplateData {
    private final TemplateParameters parameters;

    public DockerComposeTemplateData(String appName, DatabaseType commandDatabaseType, DatabaseType queryDatabaseType) {
        parameters = TemplateParameters.with(TemplateParameter.DOCKER_COMPOSE_FILE, true)
                .and(TemplateParameter.DOCKER_COMPOSE_SERVICES, new ArrayList<String>());

        if(!commandDatabaseType.equals(DatabaseType.IN_MEMORY))
            this.dependOn(DockerComposeCommandDatabaseTemplateData.from(appName, commandDatabaseType));
        if(!queryDatabaseType.equals(DatabaseType.IN_MEMORY))
            this.dependOn(DockerComposeQueryDatabaseTemplateData.from(appName, queryDatabaseType));
    }

    public DockerComposeTemplateData(String appName, DatabaseType databaseType) {
        parameters = TemplateParameters.with(TemplateParameter.DOCKER_COMPOSE_FILE, true)
            .and(TemplateParameter.DOCKER_COMPOSE_SERVICES, new ArrayList<String>());

        this.dependOn(DockerComposeDatabaseTemplateData.from(appName, databaseType));
    }

    @Override
    public void handleDependencyOutcome(TemplateStandard standard, String outcome) {
        this.parameters.<List<String>>find(TemplateParameter.DOCKER_COMPOSE_SERVICES).add(outcome);
    }

    @Override
    public TemplateParameters parameters() {
        return parameters;
    }

    @Override
    public TemplateStandard standard() {
        return JavaTemplateStandard.DOCKER_COMPOSE;
    }
}
