// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

public class QueriesDetail {

  public static String resolveQueryByIdMethodName(final String aggregateProtocol) {
    return aggregateProtocol + "Of";
  }

  public static String resolveQueryAllMethodName(final String aggregateProtocol) {
    return aggregateProtocol.endsWith("s") ? aggregateProtocol : aggregateProtocol + "s";
  }

}
