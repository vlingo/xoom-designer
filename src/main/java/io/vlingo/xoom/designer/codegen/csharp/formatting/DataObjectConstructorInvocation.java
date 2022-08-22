// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.MethodScope;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;

import java.beans.Introspector;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class DataObjectConstructorInvocation implements Formatters.Arguments {

  @Override
  public String format(final CodeGenerationParameter parent, final MethodScope scope) {
    final String carrierName = resolveCarrierName(parent);
    return parent.retrieveAllRelated(resolveFieldLabel(parent))
        .map(field -> resolveParameterName(carrierName, field, scope))
        .collect(Collectors.joining(", "));
  }

  private ParameterLabel resolveFieldLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE)) {
      return Label.STATE_FIELD;
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to format static method parameters from " + parent.label);
  }

  private String resolveParameterName(final String carrierName, final CodeGenerationParameter field,
                                      final MethodScope scope) {
    if (scope.isInstance() || ValueObjectDetail.isValueObject(field) || FieldDetail.isCollection(field)) {
      return field.value;
    }
    return carrierName + "." + toPascalCase(field.value);
  }

  private String resolveCarrierName(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(Label.AGGREGATE)) {
      return Introspector.decapitalize(CsharpTemplateStandard.AGGREGATE_STATE.resolveClassname(carrier.value));
    }
    if (carrier.isLabeled(Label.VALUE_OBJECT)) {
      return Introspector.decapitalize(carrier.value);
    }
    throw new IllegalArgumentException("Unable to resolve carrier name from " + carrier.label);
  }
}
