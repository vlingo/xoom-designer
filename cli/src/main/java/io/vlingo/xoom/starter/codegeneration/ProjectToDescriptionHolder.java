// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

import java.text.MessageFormat;
import java.util.List;

public class ProjectToDescriptionHolder {
  private final String entityClass;
  private final List<String> eventTypes;

  public ProjectToDescriptionHolder(final String entityClass, final List<String> eventTypes) {
    this.entityClass = entityClass;
    this.eventTypes = eventTypes;
  }

  public String getProjectToDescription() {
    final StringBuilder builder = new StringBuilder();
    String parameterSeparator = "";

    for (final String eventType : eventTypes) {
      builder.append(parameterSeparator).append(eventType).append(".class");
      parameterSeparator = ", ";
    }

    return MessageFormat.format("ProjectToDescription.with({0}Projection.class, {1})", entityClass, builder.toString());
  }
}
