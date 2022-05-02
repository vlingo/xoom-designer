// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.template.ParameterKey;

public enum TemplateParameter implements ParameterKey {

  PACKAGE_NAME("packageName"),
  APPLICATION_NAME("appName"),
  COLLECTION_MUTATIONS("collectionMutations"),
  AGGREGATES("aggregates"),
  AGGREGATE_PROTOCOL_NAME("aggregateProtocolName"),
  AGGREGATE_PROTOCOL_VARIABLE("aggregateProtocolVariable"),
  DOMAIN_EVENT_NAME("domainEventName"),
  ENTITY_NAME("entityName"),
  ENTITY_CREATION_METHOD("entityCreationMethodName"),
  STATE_DATA_OBJECT_NAME("dataName"),
  IMPORTS("imports"),
  STATE_NAME("stateName"),
  ID_NAME("idName"),
  ID_TYPE("idType"),
  USE_CQRS("useCQRS"),
  MEMBERS("members"),
  MEMBER_NAMES("memberNames"),
  MEMBERS_ASSIGNMENT("membersAssignment"),
  METHODS("methods"),
  METHOD_NAME("methodName"),
  METHOD_SCOPE("methodScope"),
  METHOD_PARAMETERS("methodParameters"),
  METHOD_INVOCATION_PARAMETERS("methodInvocationParameters"),
  CONSTRUCTOR_PARAMETERS("constructorParameters");

  public final String key;

  TemplateParameter(String key) {
    this.key = key;
  }

  @Override
  public String value() {
    return key;
  }

}
