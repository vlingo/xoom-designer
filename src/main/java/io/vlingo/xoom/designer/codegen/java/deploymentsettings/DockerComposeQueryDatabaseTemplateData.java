package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.storage.DatabaseType;

public class DockerComposeQueryDatabaseTemplateData extends TemplateData {
    private final TemplateParameters parameters;

    public DockerComposeQueryDatabaseTemplateData(String appName, DatabaseType queryDatabaseType) {
        this.parameters = TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName)
                .and(TemplateParameter.MODEL_CATEGORY, "query")
                .and(TemplateParameter.DATABASE_SERVICE, queryDatabaseType.label);
    }

    public static DockerComposeQueryDatabaseTemplateData from(String appName, DatabaseType queryDatabaseType) {
        return new DockerComposeQueryDatabaseTemplateData(appName, queryDatabaseType);
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
