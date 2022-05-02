// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.model;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class FieldDetail {

  private static final String UNKNOWN_FIELD_MESSAGE = "%s is not a field in %s state";

  public static String typeOf(final CodeGenerationParameter parent, final String fieldName, final CollectionMutation collectionMutation) {
    final String type = typeOf(parent, fieldName);
    return collectionMutation.isSingleParameterBased() ? genericTypeOf(type) : type;
  }

  @SuppressWarnings("static-access")
  public static String typeOf(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(resolveFieldTypeLabel(parent))
            .filter(stateField -> stateField.value.equals(fieldName))
            .map(resolveStateFieldType()).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(UNKNOWN_FIELD_MESSAGE.format(fieldName, parent.value)));
  }

  public static String genericTypeOf(final String fieldType) {
    return fieldType.split("<")[1].replace(">", "");
  }

  public static String resolveDefaultValue(final CodeGenerationParameter parent, final String stateFieldName) {
    final String type = typeOf(parent, stateFieldName);
    if (isBoolean(type)) {
      return "false";
    }
    if (isByte(type)) {
      return "(byte) 0";
    }
    if (isNumeric(type)) {
      return "0";
    }
    if (isList(type)) {
      return "new " + type + "()";
    }
    if (isSet(type)) {
      return "new Hash"+ type.substring(1) +"()";
    }
    if (isDateTime(type)) {
      return type + ".Now";
    }
    if (isChar(type)) {
      return "'\u0000'";
    }
    return "null";
  }

  private static boolean isList(final String type) {
    return type.startsWith("List") && containsGenericsType(type);
  }

  private static boolean isSet(final String type) {
    return type.startsWith("ISet") && containsGenericsType(type);
  }

  private static boolean containsGenericsType(final String type) {
    return type.contains("<") && type.endsWith(">");
  }

  public static boolean isScalar(final String fieldType) {
    if (fieldType == null || fieldType.isEmpty()) {
      throw new IllegalArgumentException("Unable to find field type");
    }
    return CodeGenerationProperties.SCALAR_TYPES.contains(fieldType.toLowerCase());
  }

  public static String resolveCollectionType(final CodeGenerationParameter field) {
    if(!field.hasAny(Label.COLLECTION_TYPE)) {
      throw new UnsupportedOperationException(field.value + " is not a Collection");
    }
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    final String collectionType = field.retrieveRelatedValue(Label.COLLECTION_TYPE);
    final String genericsType = StringUtils.capitalize(resolveWrapperType(fieldType));
    if(collectionType.contains("Set"))
      return String.format("I%s<%s>", collectionType, genericsType);
    else
      return String.format("%s<%s>", collectionType, genericsType);
  }

  public static Boolean requireImmediateInstantiation(final CodeGenerationParameter field) {
    final CollectionMutation collectionMutation =
            field.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

    return field.hasAny(Label.COLLECTION_TYPE) && !collectionMutation.isSingleParameterBased();
  }

  private static String resolveWrapperType(final String fieldType) {
    return fieldType.equalsIgnoreCase("int") ? "int" : fieldType;
  }

  public static boolean isCollection(final CodeGenerationParameter field) {
    return field.hasAny(Label.COLLECTION_TYPE);
  }

  public static boolean isCollection(final String fieldType) {
    return isList(fieldType) || isSet(fieldType);
  }

  public static boolean isValueObjectCollection(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    return isCollection(field) && !isScalar(fieldType) && !isDateTime(fieldType);
  }

  public static String resolveImportForType(final CodeGenerationParameter field) {
    if(isCollection(field)) {
      return CodeGenerationProperties.SPECIAL_TYPES_IMPORTS.get(field.retrieveRelatedValue(Label.COLLECTION_TYPE));
    }
    return CodeGenerationProperties.SPECIAL_TYPES_IMPORTS.getOrDefault(field.retrieveRelatedValue(Label.FIELD_TYPE), "");
  }

  public static boolean isDateTime(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    return isDateTime(fieldType);
  }

  public static boolean isDateTime(final String fieldType) {
    return CodeGenerationProperties.DATE_TIME_TYPES.contains(fieldType);
  }

  public static boolean isNumeric(final String fieldType) {
    return CodeGenerationProperties.SCALAR_NUMERIC_TYPES.contains(fieldType.toLowerCase());
  }

  public static boolean isByte(final String fieldType) {
    return fieldType.equalsIgnoreCase(Byte.class.getSimpleName());
  }

  public static boolean isBoolean(final String fieldType) {
    return fieldType.equalsIgnoreCase(Boolean.class.getSimpleName());
  }

  public static boolean isString(final String fieldType) {
    return fieldType.equalsIgnoreCase(String.class.getSimpleName());
  }

  public static boolean isChar(final String fieldType) {
    return fieldType.equalsIgnoreCase(CodeGenerationProperties.CHAR_TYPE);
  }

  private static Function<CodeGenerationParameter, String> resolveStateFieldType() {
    return stateField -> {
      final String fieldType = stateField.retrieveRelatedValue(Label.FIELD_TYPE);
      return isCollection(stateField) ? resolveCollectionType(stateField) : fieldType;
    };
  }

  private static Label resolveFieldTypeLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
      return Label.STATE_FIELD;
    }
    throw new IllegalArgumentException("Unable to resolve field type of " + parent.label);
  }
}
