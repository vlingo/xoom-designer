// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

public class Field {

  public final String name;
  public final String type;

  public Field(final CodeGenerationParameter fieldParameter) {
    this.name = fieldParameter.value;
    this.type = fieldParameter.retrieveRelatedValue(Label.FIELD_TYPE);
  }
}
