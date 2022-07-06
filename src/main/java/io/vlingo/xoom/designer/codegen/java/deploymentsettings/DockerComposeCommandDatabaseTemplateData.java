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

public class DockerComposeCommandDatabaseTemplateData extends TemplateData {
    private final TemplateParameters parameters;

    public DockerComposeCommandDatabaseTemplateData(String appName, DatabaseType commandDatabaseType) {
        this.parameters = TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName)
                .and(TemplateParameter.MODEL_CATEGORY, "command")
                .and(TemplateParameter.DATABASE_SERVICE, commandDatabaseType.label);
    }

    public static DockerComposeCommandDatabaseTemplateData from(String appName, DatabaseType commandDatabaseType) {
        return new DockerComposeCommandDatabaseTemplateData(appName, commandDatabaseType);
    }

    @Override
    public TemplateParameters parameters() {
        return parameters;
    }

    @Override
    public TemplateStandard standard() {
        return JavaTemplateStandard.DOCKER_COMPOSE_DATABASE;
    }
}
