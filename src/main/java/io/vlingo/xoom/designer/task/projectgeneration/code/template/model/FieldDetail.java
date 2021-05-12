// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.template.model;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import org.apache.commons.lang3.StringUtils;

import static io.vlingo.xoom.designer.task.projectgeneration.code.CodeGenerationSetup.SCALAR_NUMERIC_TYPES;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.*;

public class FieldDetail {

  private static String UNKNOWN_FIELD_MESSAGE = "%s is not a field in %s state";

  @SuppressWarnings("static-access")
  public static String typeOf(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(resolveFieldTypeLabel(parent))
            .filter(stateField -> stateField.value.equals(fieldName))
            .map(stateField -> {
              final String fieldType = stateField.retrieveRelatedValue(FIELD_TYPE);
              return isCollection(stateField) ? resolveCollectionType(stateField) : fieldType;
            }).findFirst()
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
    if (isList(type)) {
      return "new ArrayList<>()";
    }
    if (isSet(type)) {
      return "new HashSet<>()";
    }
    if (isDateTime(type)) {
      return type + ".now()";
    }
    return "null";
  }

  private static boolean isList(final String type) {
    return type.startsWith("List") && containsGenericsType(type);
  }

  private static boolean isSet(final String type) {
    return type.startsWith("Set") && containsGenericsType(type);
  }

  private static boolean containsGenericsType(final String type) {
    return type.contains("<") && type.endsWith(">");
  }

  public static boolean isScalar(final CodeGenerationParameter field) {
    if(field.hasAny(COLLECTION_TYPE)) {
      return false;
    }
    return isScalar(field.retrieveRelatedValue(FIELD_TYPE));
  }

  public static boolean isScalar(final String fieldType) {
    if (fieldType == null || fieldType.isEmpty()) {
      throw new IllegalArgumentException("Unable to find field type");
    }
    return CodeGenerationSetup.SCALAR_TYPES.contains(fieldType.toLowerCase());
  }

  public static String resolveCollectionType(final CodeGenerationParameter field) {
    if(!field.hasAny(COLLECTION_TYPE)) {
      throw new UnsupportedOperationException(field.value + " is not a Collection");
    }
    final String fieldType = field.retrieveRelatedValue(FIELD_TYPE);
    final String collectionType = field.retrieveRelatedValue(COLLECTION_TYPE);
    final String genericsType = StringUtils.capitalize(resolveWrapperType(fieldType));
    return String.format("%s<%s>", collectionType, genericsType);
  }

  public static Boolean requireImmediateInstantiation(final CodeGenerationParameter field) {
    return field.hasAny(COLLECTION_TYPE);
  }

  private static String resolveWrapperType(final String fieldType) {
    return fieldType.equalsIgnoreCase("int") ? "Integer" : fieldType;
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

  public static boolean isCollection(final CodeGenerationParameter field) {
    return field.hasAny(COLLECTION_TYPE);
  }

  public static boolean isCollection(final String fieldType) {
    return isList(fieldType) || isSet(fieldType);
  }

  public static boolean isCollectionOrDate(final CodeGenerationParameter field) {
    return isCollection(field) || isDateTime(field);
  }

  public static boolean isValueObjectCollection(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(FIELD_TYPE);
    return isCollection(field) && !isScalar(fieldType) && !isDateTime(fieldType);
  }

  public static String resolveImportForType(final CodeGenerationParameter field) {
    if(isCollection(field)) {
      return CodeGenerationSetup.SPECIAL_TYPES.get(field.retrieveRelatedValue(COLLECTION_TYPE));
    }
    return CodeGenerationSetup.SPECIAL_TYPES.getOrDefault(field.retrieveRelatedValue(FIELD_TYPE), "");
  }

  public static boolean isDateTime(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(FIELD_TYPE);
    return isDateTime(fieldType);
  }

  public static boolean isDateTime(final String fieldType) {
    return CodeGenerationSetup.DATE_TIME_TYPES.contains(fieldType);
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
