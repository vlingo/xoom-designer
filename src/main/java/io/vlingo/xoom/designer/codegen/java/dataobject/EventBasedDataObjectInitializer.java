// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.dataobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.JavaTemplateStandard;
import io.vlingo.xoom.designer.codegen.java.formatting.Formatters;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventBasedDataObjectInitializer extends Formatters.Variables<List<String>> {

  private final String carrierName;

  public EventBasedDataObjectInitializer(final String carrierName) {
    this.carrierName = carrierName;
  }

  @Override
  public List<String> format(final CodeGenerationParameter event,
                             final Stream<CodeGenerationParameter> fields) {
    return event.retrieveAllRelated(Label.STATE_FIELD)
        .filter(FieldDetail::isAssignableToValueObject)
        .map(field -> buildInitializationStatement(field))
        .collect(Collectors.toList());
  }

  private String buildInitializationStatement(final CodeGenerationParameter stateField) {
    final String fieldAlias =
            stateField.hasAny(Label.ALIAS) ? stateField.retrieveRelatedValue(Label.ALIAS) : stateField.value;

    final String fieldType =
            stateField.retrieveRelatedValue(Label.FIELD_TYPE);

    final String dataObjectName =
            JavaTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);

    final String fieldReferencePath =
            String.format("%s.%s", carrierName, fieldAlias);

    return String.format("final %s %s = %s.from(%s);", dataObjectName, fieldAlias, dataObjectName, fieldReferencePath);
  }

}
