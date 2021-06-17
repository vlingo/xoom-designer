// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.reactjs;

import io.vlingo.xoom.codegen.template.ParameterKey;

public enum TemplateParameter implements ParameterKey {

  METHOD("method"),
  AGGREGATE("aggregate"),
  AGGREGATES("aggregates"),
  ARTIFACT_ID("artifactId"),
  GROUP_ID("groupId"),
  ROUTE("route"),
  FIELD_TYPES("fieldTypes"),
  VALUE_TYPES("valueTypes"),
  TURBO_SETTINGS("turboSettings"),
  VERSION("version");

  private final String key;

  TemplateParameter(final String key) {
    this.key = key;
  }

  @Override
  public String value() {
    return key;
  }

}
