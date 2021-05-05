// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model;

import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import static io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup.SCALAR_NUMERIC_TYPES;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class FieldDetail {

  private static String UNKNOWN_FIELD_MESSAGE = "%s is not a field in %s state";

  @SuppressWarnings("static-access")
  public static String typeOf(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(resolveFieldTypeLabel(parent))
            .filter(stateField -> stateField.value.equals(fieldName))
            .map(stateField -> stateField.retrieveRelatedValue(FIELD_TYPE)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(UNKNOWN_FIELD_MESSAGE.format(fieldName, parent.value)));
  }

  public static String resolveDefaultValue(final CodeGenerationParameter parent, final String stateFieldName) {
    final String type = typeOf(parent, stateFieldName);
    if (isBoolean(type)) {
      return "false";
    }
    if (isNumeric(type)) {
      return "0";
    }
    return "null";
  }

  public static boolean isScalar(final CodeGenerationParameter field) {
    return isScalar(field.retrieveRelatedValue(FIELD_TYPE));
  }

  public static boolean isScalar(final String fieldType) {
    if (fieldType == null || fieldType.isEmpty()) {
      throw new IllegalArgumentException("Unable to find field type");
    }
    return CodeGenerationSetup.SCALAR_TYPES.contains(fieldType.toLowerCase());
  }

  public static boolean hasStringType(final CodeGenerationParameter field) {
    return isString(field.retrieveRelatedValue(FIELD_TYPE));
  }

  public static boolean hasNumericType(final CodeGenerationParameter field) {
    return isNumeric(field.retrieveRelatedValue(FIELD_TYPE));
  }

  public static boolean hasBooleanType(final CodeGenerationParameter field) {
    return isBoolean(field.retrieveRelatedValue(FIELD_TYPE));
  }

  public static boolean isNumeric(final String fieldType) {
    return SCALAR_NUMERIC_TYPES.contains(fieldType.toLowerCase());
  }

  public static boolean isBoolean(final String fieldType) {
    return fieldType.equalsIgnoreCase(Boolean.class.getSimpleName());
  }

  public static boolean isString(final String fieldType) {
    return fieldType.equalsIgnoreCase(String.class.getSimpleName());
  }

  private static Label resolveFieldTypeLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(AGGREGATE)) {
      return STATE_FIELD;
    }
    if (parent.isLabeled(VALUE_OBJECT)) {
      return VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to resolve field type of " + parent.label);
  }
}
