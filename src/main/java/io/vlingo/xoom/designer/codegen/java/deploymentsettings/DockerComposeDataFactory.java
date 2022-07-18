// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.deploymentsettings;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.storage.DatabaseType;

import java.util.Collections;
import java.util.List;

public class DockerComposeDataFactory {
  static List<TemplateData> from(CodeGenerationContext context) {
    final String appName = context.parameterOf(Label.APPLICATION_NAME);
    final Boolean useCQRS = context.parameterOf(Label.CQRS, Boolean::valueOf);

    if (useCQRS) {
      final DatabaseType commandDatabaseType = context.parameterOf(Label.COMMAND_MODEL_DATABASE, DatabaseType::valueOf);
      final DatabaseType queryDatabaseType = context.parameterOf(Label.QUERY_MODEL_DATABASE, DatabaseType::valueOf);

      return Collections.singletonList(new DockerComposeTemplateData(appName, commandDatabaseType, queryDatabaseType));
    } else {
      final DatabaseType databaseType = context.parameterOf(Label.DATABASE, DatabaseType::valueOf);

      return Collections.singletonList(new DockerComposeTemplateData(appName, databaseType));
    }
  }
}
