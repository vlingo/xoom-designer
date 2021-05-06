// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;
import static java.util.stream.Collectors.toList;

public class DataObjectConstructorAssignment extends Formatters.Fields<List<String>> {

  @Override
  public List<String> format(final CodeGenerationParameter carrier,
                             final Stream<CodeGenerationParameter> fields) {
    return carrier.retrieveAllRelated(resolveFieldLabel(carrier)).map(this::formatAssignment).collect(toList());
  }

  private String formatAssignment(final CodeGenerationParameter field) {
    if(FieldDetail.isCollection(field)) {
      return String.format("this.%s.addAll(%s);", field.value, field.value);
    }
    return String.format("this.%s = %s;", field.value, field.value);
  }

  private Label resolveFieldLabel(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(AGGREGATE)) {
      return STATE_FIELD;
    }
    if (carrier.isLabeled(VALUE_OBJECT)) {
      return VALUE_OBJECT_FIELD;
    }
    throw new UnsupportedOperationException("Unable to format fields assignment from " + carrier.label);
  }

}
