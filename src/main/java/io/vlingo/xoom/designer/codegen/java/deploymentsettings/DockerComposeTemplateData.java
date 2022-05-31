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

    public DockerComposeTemplateData(String appName, Boolean useCQRS, DatabaseType commandDatabaseType,
                                     DatabaseType queryDatabaseType) {
        parameters = TemplateParameters.with(TemplateParameter.DOCKER_COMPOSE_FILE, true)
                .and(TemplateParameter.DOCKER_COMPOSE_SERVICES, new ArrayList<String>());

        if (!useCQRS) return;

        if(!commandDatabaseType.equals(DatabaseType.IN_MEMORY))
            this.dependOn(DockerComposeCommandDatabaseTemplateData.from(appName, commandDatabaseType));
        if(!queryDatabaseType.equals(DatabaseType.IN_MEMORY))
            this.dependOn(DockerComposeQueryDatabaseTemplateData.from(appName, queryDatabaseType));
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
