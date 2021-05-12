// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;

import java.util.stream.Collectors;

public class QueriesMethodInvocation implements Formatters.Arguments {

  @Override
  public String format(final CodeGenerationParameter parent, final MethodScope scope) {
    return parent.retrieveAllRelated(Label.METHOD_PARAMETER)
            .map(methodParameter -> methodParameter.value)
            .collect(Collectors.joining(", "));
  }
}
