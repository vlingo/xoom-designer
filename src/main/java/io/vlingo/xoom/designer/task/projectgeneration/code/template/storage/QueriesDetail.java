// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.storage;

import java.beans.Introspector;

public class QueriesDetail {

  public static String resolveQueryByIdMethodName(final String aggregateProtocol) {
    return Introspector.decapitalize(aggregateProtocol) + "Of";
  }

  public static String resolveQueryAllMethodName(final String aggregateProtocol) {
    final String formatted = Introspector.decapitalize(aggregateProtocol);
    return formatted.endsWith("s") ? formatted : formatted + "s";
  }

}
