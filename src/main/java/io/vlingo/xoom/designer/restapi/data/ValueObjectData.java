// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class ValueObjectData {

  public final String name;
  public final List<ValueObjectFieldData> fields = new ArrayList<>();

  public ValueObjectData(final String name, final List<ValueObjectFieldData> fields) {
    this.name = name;
    this.fields.addAll(fields);
  }

}
