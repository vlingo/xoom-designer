// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest.resource;

import java.util.Collections;
import java.util.List;

public class PreliminaryStatement {

  private static final String REST_ASSURED = "%s firstData = given()\n" +
      "        .when()\n" +
      "        .body(%s)\n" +
      "        .%s(\"%s\")\n" +
      "        .then()\n" +
      "        .statusCode(201)\n" +
      "        .extract()\n" +
      "        .body()\n" +
      "        .as(%s.class);";

  public static List<String> with(final String testMethodName, final String valueObjectData, final String rootPath, final String rootMethod) {
    final String testDataVariableName = TestDataFormatter.formatStaticVariableName(1, testMethodName);
    return Collections.singletonList(String.format(REST_ASSURED, valueObjectData, testDataVariableName, rootMethod,
        rootPath.replace("{id}", "\"+"+testDataVariableName + ".id"+"+\""), valueObjectData));
  }

}
