// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;

import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;
import static java.util.stream.Collectors.toList;

public class DataObjectConstructorAssignment extends Formatters.Fields<List<String>> {

  private static final String LIST_ASSIGNMENT = "%s.AddRange(%s);";
  private static final String VARIABLE_ASSIGNMENT = "%s = %s;";

  @Override
  public List<String> format(final CodeGenerationParameter carrier, final Stream<CodeGenerationParameter> fields) {
    return carrier.retrieveAllRelated(resolveFieldLabel(carrier)).map(this::formatAssignment).collect(toList());
  }

  private String formatAssignment(final CodeGenerationParameter field) {
    if(FieldDetail.isCollection(field)) {
      return String.format(LIST_ASSIGNMENT, toPascalCase(field.value), field.value);
    }
    return String.format(VARIABLE_ASSIGNMENT, toPascalCase(field.value), field.value);
  }

  private Label resolveFieldLabel(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(Label.AGGREGATE)) {
      return Label.STATE_FIELD;
    }
    if (carrier.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new UnsupportedOperationException("Unable to format fields assignment from " + carrier.label);
  }

}
