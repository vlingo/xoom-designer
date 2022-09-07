// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.csharp.unittest.TestDataValueGenerator;

import java.util.List;

public class DataDeclaration {

  public static String generate(CodeGenerationParameter aggregate, List<CodeGenerationParameter> valueObjects) {
    final TestDataValueGenerator.TestDataValues testDataValues = TestDataValueGenerator
        .with(2, "", aggregate, valueObjects).generate();

    return "";
  }
}
