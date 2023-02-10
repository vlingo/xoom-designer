// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;

import java.util.Locale;

public class FieldDetail {

  private static final String UNKNOWN_FIELD_MESSAGE = "%s is not a field in %s state";

  public static String typeOf(final CodeGenerationParameter parent, final String fieldName, final CollectionMutation collectionMutation) {
    final String type = typeOf(parent, fieldName);
    return collectionMutation.isSingleParameterBased() ? genericTypeOf(type) : primitiveOrValueObjectTypeOf(type);
  }

  private static String primitiveOrValueObjectTypeOf(String type) {
    if (isString(type))
      return type.toLowerCase(Locale.ROOT);
    else
      return type;
  }

  @SuppressWarnings("static-access")
  public static String typeOf(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(resolveFieldTypeLabel(parent))
        .filter(stateField -> stateField.value.equals(fieldName))
        .map(FieldDetail::resolveStateFieldType).findFirst()
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
      return "new Hash" + type.substring(1) + "()";
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

  public static boolean isScalar(final CodeGenerationParameter field) {
    if (field.hasAny(Label.COLLECTION_TYPE)) {
      return false;
    }
    return isScalar(field.retrieveRelatedValue(Label.FIELD_TYPE));
  }

  public static boolean isScalar(final String fieldType) {
    if (fieldType == null || fieldType.isEmpty()) {
      throw new IllegalArgumentException("Unable to find field type");
    }
    return CodeGenerationProperties.SCALAR_TYPES.contains(fieldType.toLowerCase());
  }

  public static boolean isMethodParameterAssignableToScalar(final CodeGenerationParameter field, final CodeGenerationParameter methodParameter) {
    final String type = typeOf(field.parent(), field.value);
    if (isScalar(type)) {
      return true;
    }
    final CollectionMutation collectionMutation = methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);
    return isScalarTypedCollection(field) && collectionMutation.isSingleParameterBased();
  }

  public static boolean isMethodParameterAssignableToValueObject(final CodeGenerationParameter field, final CodeGenerationParameter methodParameter) {
    if (ValueObjectDetail.isValueObject(field)) {
      return true;
    }
    final CollectionMutation collectionMutation = methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);
    return isValueObjectCollection(field) && collectionMutation.isSingleParameterBased();
  }

  public static boolean isAssignableToValueObject(final CodeGenerationParameter field) {
    return isMethodParameterAssignableToValueObject(field, field);
  }

  public static String resolveCollectionType(final CodeGenerationParameter field) {
    if (!field.hasAny(Label.COLLECTION_TYPE)) {
      throw new UnsupportedOperationException(field.value + " is not a Collection");
    }
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    final String collectionType = field.retrieveRelatedValue(Label.COLLECTION_TYPE);
    final String genericsType = FieldDetail.isValueObjectCollection(field) ? resolveWrapperType(fieldType) : toCamelCase(fieldType);
    if (collectionType.contains("Set"))
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

  public static boolean hasStringType(final CodeGenerationParameter field) {
    return isString(field.retrieveRelatedValue(Label.FIELD_TYPE));
  }

  public static boolean hasNumericType(final CodeGenerationParameter field) {
    return isNumeric(field.retrieveRelatedValue(Label.FIELD_TYPE));
  }

  public static boolean hasBooleanType(final CodeGenerationParameter field) {
    return isBoolean(field.retrieveRelatedValue(Label.FIELD_TYPE));
  }

  public static boolean hasCharType(final CodeGenerationParameter field) {
    return isChar(field.retrieveRelatedValue(Label.FIELD_TYPE));
  }

  public static boolean isCollection(final CodeGenerationParameter field) {
    return field.hasAny(Label.COLLECTION_TYPE);
  }

  public static boolean isScalarTypedCollection(final CodeGenerationParameter field) {
    return isCollection(field) && !isValueObjectCollection(field);
  }

  public static boolean isSetTypedCollection(final CodeGenerationParameter field) {
    return isCollection(field) && field.retrieveRelatedValue(Label.COLLECTION_TYPE).equalsIgnoreCase("Set");
  }

  public static boolean isCollection(final String fieldType) {
    return isList(fieldType) || isSet(fieldType);
  }

  public static boolean isCollectionOrDate(final CodeGenerationParameter field) {
    return isCollection(field) || isDateTime(field);
  }

  public static boolean isValueObjectCollection(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    return isCollection(field) && !isScalar(fieldType) && !isDateTime(fieldType);
  }

  public static String resolveImportForType(final CodeGenerationParameter field) {
    final String key = field.retrieveRelatedValue(Label.FIELD_TYPE);
    return CodeGenerationProperties.CSHARP_SPECIAL_TYPES_IMPORTS.getOrDefault(key, "");
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
    return fieldType.equalsIgnoreCase(Boolean.class.getSimpleName()) || fieldType.equalsIgnoreCase("bool");
  }

  public static boolean isString(final String fieldType) {
    return fieldType.equalsIgnoreCase(String.class.getSimpleName());
  }

  public static boolean isChar(final String fieldType) {
    return fieldType.equalsIgnoreCase(CodeGenerationProperties.CHAR_TYPE);
  }

  public static String toCamelCase(String name) {
    return toCamelCase(name, false);
  }

  public static String toPascalCase(String name) {
    return toCamelCase(name, true);
  }

  private static String toCamelCase(String name, boolean capNext) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < name.length(); ++i) {
      char c = name.charAt(i);
      if ('a' <= c && c <= 'z') {
        if (capNext) {
          sb.append((char) (c + -32));
        } else {
          sb.append(c);
        }

        capNext = false;
      } else if ('A' <= c && c <= 'Z') {
        if (i == 0 && !capNext) {
          sb.append((char) (c - -32));
        } else {
          sb.append(c);
        }

        capNext = false;
      } else if ('0' <= c && c <= '9') {
        sb.append(c);
        capNext = true;
      } else {
        capNext = true;
      }
    }

    return sb.toString();
  }

  private static String resolveStateFieldType(final CodeGenerationParameter stateField) {
    final String fieldType = stateField.retrieveRelatedValue(Label.FIELD_TYPE);
    if (isCollection(stateField)) return resolveCollectionType(stateField);
    if (ValueObjectDetail.isValueObject(stateField) || isDateTime(stateField)) return fieldType;
    return toCamelCase(fieldType, false);
  }

  private static Label resolveFieldTypeLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
      return Label.STATE_FIELD;
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to resolve field type of " + parent.label);
  }

  public static String genericTypeOf(final CodeGenerationParameter parent, final String fieldName) {
    return genericTypeOf(typeOf(parent, fieldName));
  }
}
