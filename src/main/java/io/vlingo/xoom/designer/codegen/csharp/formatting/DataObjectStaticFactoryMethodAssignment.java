// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.beans.Introspector;
import java.util.List;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;
import static java.util.stream.Collectors.toList;

public class DataObjectStaticFactoryMethodAssignment extends Formatters.Variables<List<String>> {

  @Override
  public List<String> format(final CodeGenerationParameter carrier, final Stream<CodeGenerationParameter> fields) {
    return carrier.retrieveAllRelated(resolveFieldLabel(carrier))
        .filter(field -> ValueObjectDetail.isValueObject(field) || FieldDetail.isCollection(field))
        .map(field -> formatAssignment(carrier, field)).collect(toList());
  }

  private String formatAssignment(final CodeGenerationParameter carrier, final CodeGenerationParameter field) {
    final String dataObjectName = resolveDataObjectName(field);

    final String fieldAccessExpression = resolveFieldAccessExpression(carrier, field);

    final String variableDeclaration = resolveVariableDeclaration(field);

    final String alternativeStatements = resolveAlternativeStatements(field, dataObjectName, fieldAccessExpression);

    return String.format("%s = %s != null ? %s;", variableDeclaration, fieldAccessExpression, alternativeStatements);
  }

  private String resolveVariableDeclaration(final CodeGenerationParameter field) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");
    final String variableName = codeElementFormatter.rectifySyntax(field.value);

    return String.format("var %s", variableName);
  }

  private String resolveAlternativeStatements(final CodeGenerationParameter field, final String dataObjectName,
                                              final String fieldAccessExpression) {
    if (FieldDetail.isCollection(field)) {
      final String collectionType = field.retrieveRelatedValue(Label.COLLECTION_TYPE);
      final String defaultValue = FieldDetail.resolveDefaultValue(field.parent(), field.value);
      if (FieldDetail.isValueObjectCollection(field)) {
        return String.format("%s.Select(%s.From).To%s() : %s", fieldAccessExpression, dataObjectName,
            collectionType, defaultValue);
      } else {
        return String.format("%s : %s", fieldAccessExpression, defaultValue);
      }
    }
    return String.format("%s.From(%s) : null", dataObjectName, fieldAccessExpression);
  }

  private String resolveFieldAccessExpression(final CodeGenerationParameter carrier, final CodeGenerationParameter field) {
    return resolveCarrierName(carrier) + "." + toPascalCase(field.value);
  }

  private String resolveDataObjectName(final CodeGenerationParameter field) {
    return CsharpTemplateStandard.DATA_OBJECT.resolveClassname(field.retrieveRelatedValue(Label.FIELD_TYPE));
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

  private ParameterLabel resolveFieldLabel(final CodeGenerationParameter carrier) {
    if (carrier.isLabeled(Label.AGGREGATE)) {
      return Label.STATE_FIELD;
    }
    if (carrier.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new UnsupportedOperationException("Unable to format fields assignment from " + carrier.label);
  }

}
