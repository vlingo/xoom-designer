// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.designer.codegen.java.DeploymentSettings;
import io.vlingo.xoom.designer.codegen.java.projections.ProjectionType;
import io.vlingo.xoom.designer.codegen.java.storage.DatabaseType;
import io.vlingo.xoom.designer.codegen.java.storage.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CodeGenerationParameterValidationStep implements CodeGenerationStep {

  //complete: PACKAGE_PATTERN, ARTIFACT_PATTERN, VERSION_PATTERN, IDENTIFIER_PATTERN
  //some of the others are maybe not complete
  private static final String PACKAGE_PATTERN = "^[a-z]+(.[a-zA-Z_]([a-zA-Z_$#\\d])*)+$";
  private static final String ARTIFACT_PATTERN = "^[a-z-]+$";
  private static final String VERSION_PATTERN = "^\\d+.\\d+.\\d+$";
  private static final String CLASSNAME_PATTERN = "^[A-Z]+[A-Za-zÀ-ÖØ-öø-ÿ]*$";
  private static final String IDENTIFIER_PATTERN = "^[a-zA-Z_$][a-zA-ZÀ-ÖØ-öø-ÿ_$0-9]*$";
  private static final String ROUTE_PATTERN = "^[a-zA-ZÀ-ÖØ-öø-ÿ_$/?%-]+$";
  private static final String DOCKER_PATTERN = "^[a-zA-Z]+[a-zA-Z._\\d-]*$";

  @Override
  public void process(final CodeGenerationContext context) {
    List<String> errorStrings = new ArrayList<>();
    final CodeGenerationParameters parameters = context.parameters();

    if(!retrieve(parameters, Label.GROUP_ID).matches(PACKAGE_PATTERN)) errorStrings.add("GroupID must follow package pattern");
    if(!retrieve(parameters, Label.ARTIFACT_ID).matches(ARTIFACT_PATTERN)) errorStrings.add("ArtifactID must consist of lowercase letters and hyphens");
    if(!retrieve(parameters, Label.ARTIFACT_VERSION).matches(VERSION_PATTERN)) errorStrings.add("Version must be a semantic version");
    if(!retrieve(parameters, Label.PACKAGE).matches(PACKAGE_PATTERN)) errorStrings.add("Package must follow package pattern");

    if(!areValueObjectNamesValid(parameters)) errorStrings.add("Value Object names must follow classname pattern");
    if(!areValueObjectFieldsValid(parameters)) errorStrings.add("Value Object fields must follow fieldname pattern");
    if(!areAggregateNamesValid(parameters)) errorStrings.add("Aggregate names must follow classname pattern");
    if(!areStateFieldsValid(parameters)) errorStrings.add("State Fields must follow fieldname pattern");
    if(!areDomainEventsValid(parameters)) errorStrings.add("Domain Events must follow classname pattern");
    if(!areMethodsValid(parameters)) errorStrings.add("Methods are not valid");
    if(!areRestResourcesValid(parameters)) errorStrings.add("Rest Resources are not valid");

    if(!isStorageTypeValid(parameters)) errorStrings.add("StorageType is not valid");
    if(!isProjectionValid(retrieve(parameters, Label.PROJECTION_TYPE), retrieve(parameters, Label.STORAGE_TYPE))) errorStrings.add("ProjectionType is not valid");
    if(!isDatabaseValid(parameters)) errorStrings.add("Database is not valid");

    if(!isDeploymentValid(parameters)) errorStrings.add("Deployment is not valid");

    if(!isTargetFolderValid(retrieve(parameters, Label.TARGET_FOLDER))) errorStrings.add("Target folder is not valid");

    if(errorStrings.size() > 0) {
      String errorMessage = String.join(", ", errorStrings);
      throw new IllegalArgumentException(errorMessage);
    }
  }

  private String retrieve(final CodeGenerationParameters parameters, final Label label) {
    return parameters.retrieveValue(label);
  }

  private <T> T retrieveObject(final CodeGenerationParameters parameters, Label label) {
    return parameters.retrieveObject(label);
  }

  private boolean areValueObjectNamesValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.VALUE_OBJECT).allMatch(valueObject -> valueObject.value.matches(CLASSNAME_PATTERN));
  }

  private boolean areValueObjectFieldsValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.VALUE_OBJECT).flatMap(aggregate -> aggregate.retrieveAllRelated(Label.VALUE_OBJECT_FIELD))
            .allMatch(stateField -> stateField.value.matches(IDENTIFIER_PATTERN));
  }

  private boolean areAggregateNamesValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.AGGREGATE).allMatch(aggregateName -> aggregateName.value.matches(CLASSNAME_PATTERN));
  }

  private boolean areStateFieldsValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.AGGREGATE)
            .flatMap(aggregate -> aggregate.retrieveAllRelated(Label.STATE_FIELD))
            .allMatch(stateField -> stateField.value.matches(IDENTIFIER_PATTERN));
  }

  private boolean areDomainEventsValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.AGGREGATE)
            .flatMap(aggregate -> aggregate.retrieveAllRelated(Label.DOMAIN_EVENT))
            .allMatch(event -> event.value.matches(CLASSNAME_PATTERN));
  }

  private boolean areMethodsValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.AGGREGATE).allMatch(
            aggregate -> aggregate.retrieveRelatedValue(Label.AGGREGATE_METHOD).matches(IDENTIFIER_PATTERN));
  }

  private boolean areRestResourcesValid(final CodeGenerationParameters parameters) {
    return parameters.retrieveAll(Label.AGGREGATE)
            .map(aggregate ->
                    aggregate.retrieveAllRelated(Label.REST_RESOURCES).allMatch(rest ->
                            rest.value.startsWith("/") &&
                                    rest.retrieveRelatedValue(Label.ROUTE_PATH).matches(ROUTE_PATTERN) &&
                                    aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).allMatch(
                                            method -> method.value.equals(rest.retrieveRelatedValue(Label.ROUTE_SIGNATURE))) &&
                                    Stream.of("POST", "PUT", "DELETE", "PATCH", "GET", "HEAD", "OPTIONS"
                                    ).anyMatch(rest.retrieveRelatedValue(Label.ROUTE_METHOD)::equals)
                    )
            ).allMatch(bool -> bool==true);
  }

  private boolean isStorageTypeValid(final CodeGenerationParameters parameters) {
    return Stream.of(StorageType.STATE_STORE.key, StorageType.JOURNAL.key).anyMatch(retrieve(parameters, Label.STORAGE_TYPE)::equals);
  }

  private boolean isProjectionValid(String projectionType, String storageType) {
    if(storageType == StorageType.JOURNAL.key) {
      return Stream.of(ProjectionType.NONE.name(), ProjectionType.EVENT_BASED.name()).anyMatch(projectionType::equals);
    }
    return Stream.of(ProjectionType.NONE.name(), ProjectionType.EVENT_BASED.name(), ProjectionType.OPERATION_BASED.name()).anyMatch(projectionType::equals);
  }

  private boolean isDatabaseValid(final CodeGenerationParameters parameters) {
    String database = retrieve(parameters, Label.DATABASE);
    String queryDatabase = retrieve(parameters, Label.QUERY_MODEL_DATABASE);
    String commandDatabase = retrieve(parameters, Label.COMMAND_MODEL_DATABASE);
    if(database.length()>0) {
      return databases().anyMatch(database::equals);
    }
    return databases().anyMatch(queryDatabase::equals) && databases().anyMatch(commandDatabase::equals);
  }

  private Stream<String> databases() {
    return Stream.of(DatabaseType.values()).map(db -> db.name());
  }

  private boolean isDeploymentValid(final CodeGenerationParameters parameters) {
    final DeploymentSettings deploymentSettings = retrieveObject(parameters, Label.DEPLOYMENT_SETTINGS);
    boolean validImage = true;
    //no break, fall through
    switch(deploymentSettings.type) {
      case KUBERNETES: validImage = validImage &&
              deploymentSettings.kubernetesPod.matches(DOCKER_PATTERN) &&
              deploymentSettings.dockerImage.matches(DOCKER_PATTERN);
      case DOCKER: validImage = validImage &&
              deploymentSettings.dockerImage.matches(DOCKER_PATTERN);
      default:
        break;
    }
    return validImage;
  }

  private boolean isTargetFolderValid(String targetFolder) {
    //TODO: ping the filesystem and only return true if the folder is available/usable
    return targetFolder.length() >= 2;
  }
}
