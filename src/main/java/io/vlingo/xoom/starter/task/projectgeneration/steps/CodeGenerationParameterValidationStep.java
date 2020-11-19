// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.task.projectgeneration.steps;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.Label;
import io.vlingo.xoom.codegen.template.projections.ProjectionType;
import io.vlingo.xoom.codegen.template.storage.DatabaseType;
import io.vlingo.xoom.codegen.template.storage.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.vlingo.xoom.starter.task.TaskExecutionContext;
import io.vlingo.xoom.starter.task.projectgeneration.SupportedTypes;
import io.vlingo.xoom.starter.task.steps.TaskExecutionStep;

public class CodeGenerationParameterValidationStep implements TaskExecutionStep {

    //complete: PACKAGE_PATTERN, ARTIFACT_PATTERN, VERSION_PATTERN, IDENTIFIER_PATTERN
    //some of the others are maybe not complete
    private static String PACKAGE_PATTERN = "^[a-z]+(.[a-zA-Z_]([a-zA-Z_$#\\d])*)+$";
    private static String ARTIFACT_PATTERN = "^[a-z-]+$";
    private static String VERSION_PATTERN = "^\\d+.\\d+.\\d+$";
    private static String CLASSNAME_PATTERN = "^[A-Z]+[A-Za-z]*$";
    private static String IDENTIFIER_PATTERN = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
    private static String ROUTE_PATTERN = "^[a-zA-Z_$/?%]+$";
    private static String DOCKER_PATTERN = "^[a-zA-Z]+[a-zA-Z._-\\d]*$";
    private static CodeGenerationParameters parameters;

    @Override
    public void process(final TaskExecutionContext context) {
        parameters = context.codeGenerationParameters();
        List<String> errorStrings = new ArrayList<>();
        if(!retrieve(Label.GROUP_ID).matches(PACKAGE_PATTERN)) errorStrings.add("GroupID must follow package pattern");
        if(!retrieve(Label.ARTIFACT_ID).matches(ARTIFACT_PATTERN)) errorStrings.add("ArtifactID must consist of lowercase letters and hyphens");
        if(!retrieve(Label.VERSION).matches(VERSION_PATTERN)) errorStrings.add("Version must be a semantic version");
        if(!retrieve(Label.PACKAGE).matches(PACKAGE_PATTERN)) errorStrings.add("Package must follow package pattern");
        if(!isXoomVersionValid()) errorStrings.add("Xoom version must be either 1.4.1-SNAPSHOT or 1.4.0");

        if(!areAggregateNamesValid()) errorStrings.add("Aggregate names must follow classname pattern");
        if(!areStateFieldsValid()) errorStrings.add("State Fields must follow fieldname pattern");
        if(!areDomainEventsValid()) errorStrings.add("Domain Events must follow classname pattern");
        if(!areMethodsValid()) errorStrings.add("Methods are not valid");
        if(!areRestResourcesValid()) errorStrings.add("Rest Resources are not valid");

        if(!isStorageTypeValid()) errorStrings.add("StorageType is not valid");
        if(!isProjectionValid(retrieve(Label.PROJECTION_TYPE), retrieve(Label.STORAGE_TYPE))) errorStrings.add("ProjectionType is not valid");
        if(!isDatabaseValid()) errorStrings.add("Database is not valid");

        if(!isDeploymentValid()) errorStrings.add("");
        
        if(!isTargetFolderValid(retrieve(Label.TARGET_FOLDER))) errorStrings.add("");

        if(errorStrings.size() > 0) {
            String errorMessage = String.join(", ", errorStrings);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isXoomVersionValid() {
        return Stream.of("1.4.1-SNAPSHOT", "1.4.0").anyMatch(retrieve(Label.XOOM_VERSION)::equals);
    }

    private String retrieve(Label label) {
        return parameters.retrieveValue(label);
    }

    private boolean areAggregateNamesValid() {
        return parameters.retrieveAll(Label.AGGREGATE).allMatch(aggregateName -> aggregateName.value.matches(CLASSNAME_PATTERN));
    }

    private boolean areStateFieldsValid() {
        return parameters.retrieveAll(Label.AGGREGATE).map(
            aggregate -> aggregate.retrieveAllRelated(Label.STATE_FIELD).allMatch(stateField -> stateField.value.matches(IDENTIFIER_PATTERN) &&
                stateField.retrieveAllRelated(Label.FIELD_TYPE).allMatch(type -> 
                    Stream.of(
                        SupportedTypes.INTEGER.name(),
                        SupportedTypes.LONG.name(),
                        SupportedTypes.FLOAT.name(),
                        SupportedTypes.DOUBLE.name(),
                        SupportedTypes.STRING.name(),
                        SupportedTypes.BOOLEAN.name()
                    ).anyMatch(type.value::equals)
                )
            )
        ).allMatch(bool -> bool==true);
    }

    private boolean areDomainEventsValid() {
        return parameters.retrieveAll(Label.AGGREGATE).map(
            aggregate -> aggregate.retrieveAllRelated(Label.DOMAIN_EVENT).allMatch(event -> event.value.matches(CLASSNAME_PATTERN))
        ).allMatch(bool -> bool==true);
    }

    private boolean areMethodsValid() {
        return parameters.retrieveAll(Label.AGGREGATE).map(
            aggregate -> aggregate.retrieveRelatedValue(Label.AGGREGATE_METHOD).matches(IDENTIFIER_PATTERN)
        ).allMatch(bool -> bool==true);
    }

    private boolean areRestResourcesValid() {
        return parameters.retrieveAll(Label.AGGREGATE)
        .map(aggregate ->
            aggregate.retrieveAllRelated(Label.REST_RESOURCES).allMatch(rest ->
                rest.value.startsWith("/") &&
                rest.retrieveRelatedValue(Label.ROUTE_PATH).matches(ROUTE_PATTERN) &&
                aggregate.retrieveAllRelated(Label.AGGREGATE_METHOD).map(
                    method -> method.value.equals(rest.retrieveRelatedValue(Label.ROUTE_SIGNATURE))
                ).allMatch(bool -> bool==true) &&
                Stream.of(
                    "POST",
                    "PUT",
                    "DELETE",
                    "PATCH",
                    "GET",
                    "HEAD",
                    "OPTIONS"
                ).anyMatch(rest.retrieveRelatedValue(Label.ROUTE_METHOD)::equals)
            )
        ).allMatch(bool -> bool==true);
    }

    private boolean isStorageTypeValid() {
        return Stream.of(StorageType.STATE_STORE.key, StorageType.JOURNAL.key).anyMatch(retrieve(Label.STORAGE_TYPE)::equals);
    }

    private boolean isProjectionValid(String projectionType, String storageType) {
        if(storageType == StorageType.JOURNAL.key) {
            return Stream.of(ProjectionType.NONE.name(), ProjectionType.EVENT_BASED.name()).anyMatch(projectionType::equals);
        }
        return Stream.of(ProjectionType.NONE.name(), ProjectionType.EVENT_BASED.name(), ProjectionType.OPERATION_BASED.name()).anyMatch(projectionType::equals);
    }

    private boolean isDatabaseValid() {
        String database = retrieve(Label.DATABASE);
        String queryDatabase = retrieve(Label.QUERY_MODEL_DATABASE);
        String commandDatabase = retrieve(Label.COMMAND_MODEL_DATABASE);
        Stream<String> databases = Stream.of(DatabaseType.values()).map(db -> db.name());
        if(database.length()>0) {
            return databases.anyMatch(database::equals);
        }
        return databases.anyMatch(queryDatabase::equals) && databases.anyMatch(commandDatabase::equals);
    }

    private boolean isDeploymentValid() {
        String deploymentType = retrieve(Label.DEPLOYMENT);
        boolean validImage = true;
        //no break, fall through
        switch(deploymentType) {
            case "KUBERNETES": validImage = validImage && 
                retrieve(Label.KUBERNETES_IMAGE).matches(DOCKER_PATTERN) &&
                retrieve(Label.KUBERNETES_POD_NAME).matches(DOCKER_PATTERN);
            case "DOCKER": validImage = validImage &&
                retrieve(Label.DOCKER_IMAGE).matches(DOCKER_PATTERN);
        }
        return validImage && Stream.of(DeploymentType.values()).map(dt -> dt.name()).anyMatch(deploymentType::equals);
    }

    private boolean isTargetFolderValid(String targetFolder) {
        //TODO: ping the filesystem and only return true if the folder is available/usable
        if(targetFolder.length()<2) return false;
        return true;
    }
}
