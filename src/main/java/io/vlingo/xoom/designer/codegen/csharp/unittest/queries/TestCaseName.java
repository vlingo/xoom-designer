// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.unittest.queries;

import java.util.stream.Stream;

public enum TestCaseName {

  QUERY_BY_ID("QueryById"),
  QUERY_ALL("QueryAll");

  public final String method;

  TestCaseName(final String method) {
    this.method = method;
  }

  public static TestCaseName ofMethod(final String method) {
    return Stream.of(values()).filter(testCaseMethod -> testCaseMethod.method.equals(method))
            .findFirst().orElseThrow(IllegalArgumentException::new);
  }

}
