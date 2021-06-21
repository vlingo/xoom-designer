// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

import java.util.Collections;
import java.util.List;

public class DateTimeType implements Type {

  @Override
  public String typeName() {
    return "LocalDateTime";
  }

  @Override
  public List<String> qualifiedClassNames() {
    return Collections.singletonList("java.util.LocalDateTime");
  }

  @Override
  public String defaultValue() {
    return "LocalDateTime.now()";
  }

  public static boolean isDateTime(final String typeName) {
    return typeName.equals("DateTime");
  }

}
