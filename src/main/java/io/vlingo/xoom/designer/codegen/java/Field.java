// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

public class Field {

  public final String name;
  public final String type;
  public final boolean isCollection;
  public final boolean isCompositeId;

  public Field(final CodeGenerationParameter fieldParameter) {
    this.name = fieldParameter.value;
    final String collectionType = fieldParameter.retrieveRelatedValue(Label.COLLECTION_TYPE);
    this.isCollection = collectionType != null && !collectionType.isEmpty();
    this.isCompositeId = FieldDetail.isCompositeId(fieldParameter);
    this.type = fieldParameter.retrieveRelatedValue(Label.FIELD_TYPE);
  }
}
