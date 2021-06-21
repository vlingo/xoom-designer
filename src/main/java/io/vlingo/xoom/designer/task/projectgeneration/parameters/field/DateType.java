// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

import java.util.Collections;
import java.util.List;

public class DateType implements Type {

  @Override
  public String typeName() {
    return "LocalDate";
  }

  @Override
  public List<String> qualifiedClassNames() {
    return Collections.singletonList("java.util.LocalDate");
  }

  @Override
  public String defaultValue() {
    return "LocalDate.now()";
  }

  public static boolean isDate(final String typeName) {
    return typeName.equals("Date");
  }

}
