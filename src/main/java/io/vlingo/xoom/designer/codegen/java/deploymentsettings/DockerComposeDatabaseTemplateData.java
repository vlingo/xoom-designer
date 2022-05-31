package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.TemplateParameter;
import io.vlingo.xoom.designer.codegen.java.storage.DatabaseType;

public class DockerComposeDatabaseTemplateData extends TemplateData {
  private final TemplateParameters parameters;

  public DockerComposeDatabaseTemplateData(String appName, DatabaseType databaseType) {
    this.parameters = TemplateParameters.with(TemplateParameter.APPLICATION_NAME, appName)
        .and(TemplateParameter.DATABASE_SERVICE, databaseType.label);
  }

  public static DockerComposeDatabaseTemplateData from(String appName, DatabaseType databaseType) {
    return new DockerComposeDatabaseTemplateData(appName, databaseType);
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
