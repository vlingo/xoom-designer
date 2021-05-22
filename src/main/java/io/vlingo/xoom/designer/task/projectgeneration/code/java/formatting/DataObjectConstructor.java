// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.stream.Collectors;

public class DataObjectConstructor implements Formatters.Arguments {

  @Override
  public String format(final CodeGenerationParameter parent, final MethodScope scope) {
    return parent.retrieveAllRelated(resolveFieldLabel(parent))
            .map(field -> String.format("final %s %s", resolveFieldType(field), field.value))
            .collect(Collectors.joining(", "));
  }

  private Label resolveFieldLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE)) {
      return Label.STATE_FIELD;
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to format static method parameters from " + parent.label);
  }

  private String resolveFieldType(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    if (FieldDetail.isCollection(field)) {
      return DataObjectDetail.resolveCollectionType(field);
    }
    if (ValueObjectDetail.isValueObject(field)) {
      return JavaTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);
    }
    return fieldType;
  }

}
