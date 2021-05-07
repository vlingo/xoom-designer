// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.MethodScope;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.VALUE_OBJECT_FIELD;

public class ValueObjectConstructorInvocation implements Formatters.Arguments {

  @Override
  public String format(final CodeGenerationParameter valueObject, final MethodScope scope) {
    return valueObject.retrieveAllRelated(VALUE_OBJECT_FIELD)
            .map(field -> field.value).collect(Collectors.joining(", "));
  }
}
