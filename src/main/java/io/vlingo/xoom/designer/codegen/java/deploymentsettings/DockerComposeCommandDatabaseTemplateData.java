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
