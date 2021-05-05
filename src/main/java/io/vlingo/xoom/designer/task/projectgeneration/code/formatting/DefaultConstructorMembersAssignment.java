// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DefaultConstructorMembersAssignment extends Formatters.Fields<List<String>> {

  private final String carrierName;

  DefaultConstructorMembersAssignment() {
    this("");
  }

  DefaultConstructorMembersAssignment(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter aggregate,
                             final Stream<CodeGenerationParameter> fields) {
    return fields.map(field -> {
      final String valueRetrievalExpression = resolveValueRetrieval(field);
      return String.format("this.%s = %s;", field.value, valueRetrievalExpression);
    }).collect(toList());
  }

  private String resolveValueRetrieval(final CodeGenerationParameter field) {
    if (carrierName.isEmpty()) {
      return field.value;
    }
    return carrierName + "." + field.value;
  }

}
