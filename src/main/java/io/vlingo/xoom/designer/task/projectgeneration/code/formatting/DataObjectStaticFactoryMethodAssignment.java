// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.turbo.codegen.parameter.ParameterLabel;

import java.beans.Introspector;
import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.template.DesignerTemplateStandard.DATA_OBJECT;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;
import static java.util.stream.Collectors.toList;

public class DataObjectStaticFactoryMethodAssignment extends Formatters.Variables<List<String>> {

  @Override
  public List<String> format(final CodeGenerationParameter carrier,
                             final Stream<CodeGenerationParameter> fields) {
    return carrier.retrieveAllRelated(resolveFieldLabel(carrier)).filter(ValueObjectDetail::isValueObject)
            .map(field -> formatAssignment(carrier, field)).collect(toList());
  }

  private String formatAssignment(final CodeGenerationParameter carrier, final CodeGenerationParameter field) {
    final String dataObjectName =
            resolveDataObjectName(field);

    final String variableName =
            Introspector.decapitalize(field.value);

    final String fieldAccessExpression =
            resolveFieldAccessExpression(carrier, field);

    return String.format("final %s %s = %s != null ? %s.from(%s) : null;",
            dataObjectName, variableName, fieldAccessExpression, dataObjectName, fieldAccessExpression);
  }

  private String resolveFieldAccessExpression(final CodeGenerationParameter carrier, final CodeGenerationParameter field) {
    return resolveCarrierName(carrier) + "." + Introspector.decapitalize(field.value);
  }

  private String resolveDataObjectName(final CodeGenerationParameter field) {
    return DATA_OBJECT.resolveClassname(field.retrieveRelatedValue(FIELD_TYPE));
  }

  private String resolveCarrierName(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(AGGREGATE)) {
      return Introspector.decapitalize(DesignerTemplateStandard.AGGREGATE_STATE.resolveClassname(carrier.value));
    }
    if (carrier.isLabeled(VALUE_OBJECT)) {
      return Introspector.decapitalize(carrier.value);
    }
    throw new IllegalArgumentException("Unable to resolve carrier name from " + carrier.label);
  }

  private ParameterLabel resolveFieldLabel(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(AGGREGATE)) {
      return STATE_FIELD;
    }
    if (carrier.isLabeled(VALUE_OBJECT)) {
      return VALUE_OBJECT_FIELD;
    }
    throw new UnsupportedOperationException("Unable to format fields assignment from " + carrier.label);
  }

}
