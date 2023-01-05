// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.unittest.TestDataValueGenerator;

import java.util.List;

import static io.vlingo.xoom.designer.codegen.java.unittest.resource.TestCase.TEST_DATA_SET_SIZE;
import static java.util.stream.Collectors.toList;

public class DataDeclaration {

  public static String generate(CodeGenerationParameter aggregate, List<CodeGenerationParameter> valueObjects) {
    final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
        .with(TEST_DATA_SET_SIZE, "", aggregate, valueObjects).generate();

    final List<String> compositeIdFields = aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isCompositeId)
        .map(field -> field.value).collect(toList());

    if(compositeIdFields.isEmpty())
      return "";

    final List<String> values = compositeIdFields.stream()
        .map(testDataValues::retrieve).collect(toList());
    values.add("");
    return String.join(", ", values);
  }
}
