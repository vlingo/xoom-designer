// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.MethodScope;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.beans.Introspector;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.JavaTemplateStandard.AGGREGATE_STATE;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

public class DataObjectConstructorInvocation implements Formatters.Arguments {

  @Override
  public String format(final CodeGenerationParameter parent, final MethodScope scope) {
    final String carrierName = resolveCarrierName(parent);
    return parent.retrieveAllRelated(resolveFieldLabel(parent))
            .map(field -> resolveParameterName(carrierName, field, scope))
            .collect(Collectors.joining(", "));
  }

  private ParameterLabel resolveFieldLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(AGGREGATE)) {
      return STATE_FIELD;
    }
    if (parent.isLabeled(VALUE_OBJECT)) {
      return VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to format static method parameters from " + parent.label);
  }

  private String resolveParameterName(final String carrierName,
                                      final CodeGenerationParameter field,
                                      final MethodScope scope) {
    if (scope.isInstance() || ValueObjectDetail.isValueObject(field) || FieldDetail.isCollection(field)) {
      return field.value;
    }
    return carrierName + "." + field.value;
  }

  private String resolveCarrierName(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(AGGREGATE)) {
      return Introspector.decapitalize(AGGREGATE_STATE.resolveClassname(carrier.value));
    }
    if (carrier.isLabeled(VALUE_OBJECT)) {
      return Introspector.decapitalize(carrier.value);
    }
    throw new IllegalArgumentException("Unable to resolve carrier name from " + carrier.label);
  }
}
