// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.model;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties;
import io.vlingo.xoom.designer.task.projectgeneration.CollectionMutation;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;
import org.apache.commons.lang3.StringUtils;

import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties.SCALAR_NUMERIC_TYPES;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.COLLECTION_MUTATION;

public class FieldDetail {

  private static String UNKNOWN_FIELD_MESSAGE = "%s is not a field in %s state";

  public static String typeOf(final CodeGenerationParameter parent, final String fieldName, final CollectionMutation collectionMutation) {
    final String type = typeOf(parent, fieldName);
    return collectionMutation.isSingleParameterBased() ? genericTypeOf(type) : type;
  }

  @SuppressWarnings("static-access")
  public static String typeOf(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(resolveFieldTypeLabel(parent))
            .filter(stateField -> stateField.value.equals(fieldName))
            .map(stateField -> {
              final String fieldType = stateField.retrieveRelatedValue(Label.FIELD_TYPE);
              return isCollection(stateField) ? resolveCollectionType(stateField) : fieldType;
            }).findFirst()
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
    if (isChar(type)) {
      return "'\u0000'";
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
    if(field.hasAny(Label.COLLECTION_TYPE)) {
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

  public static String resolveCollectionType(final CodeGenerationParameter field) {
    if(!field.hasAny(Label.COLLECTION_TYPE)) {
      throw new UnsupportedOperationException(field.value + " is not a Collection");
    }
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    final String collectionType = field.retrieveRelatedValue(Label.COLLECTION_TYPE);
    final String genericsType = StringUtils.capitalize(resolveWrapperType(fieldType));
    return String.format("%s<%s>", collectionType, genericsType);
  }

  public static Boolean requireImmediateInstantiation(final CodeGenerationParameter field) {
    final CollectionMutation collectionMutation =
            field.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);

    return field.hasAny(Label.COLLECTION_TYPE) && !collectionMutation.isSingleParameterBased();
  }

  private static String resolveWrapperType(final String fieldType) {
    return fieldType.equalsIgnoreCase("int") ? "Integer" : fieldType;
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

  public static boolean isCollection(final String fieldType) {
    return isList(fieldType) || isSet(fieldType);
  }

  public static boolean isCollectionOrDate(final CodeGenerationParameter field) {
    return isCollection(field) || isDateTime(field);
  }

  public static boolean isScalarTypedCollection(final CodeGenerationParameter field) {
    return isCollection(field) && !isValueObjectCollection(field);
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
    return SCALAR_NUMERIC_TYPES.contains(fieldType.toLowerCase());
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

  private static Label resolveFieldTypeLabel(final CodeGenerationParameter parent) {
    if (parent.isLabeled(Label.AGGREGATE) || parent.isLabeled(Label.DOMAIN_EVENT)) {
      return Label.STATE_FIELD;
    }
    if (parent.isLabeled(Label.VALUE_OBJECT)) {
      return Label.VALUE_OBJECT_FIELD;
    }
    throw new IllegalArgumentException("Unable to resolve field type of " + parent.label);
  }

  public static boolean isMethodParameterAssignableToScalar(final CodeGenerationParameter stateField, final CodeGenerationParameter methodParameter) {
    final String type = typeOf(stateField.parent(), stateField.value);
    if(isScalar(type)) {
      return true;
    }
    final CollectionMutation collectionMutation = methodParameter.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);
    return isScalarTypedCollection(stateField) && collectionMutation.isSingleParameterBased();
  }

  public static boolean isMethodParameterAssignableToValueObject(final CodeGenerationParameter stateField, final CodeGenerationParameter methodParameter) {
    if(ValueObjectDetail.isValueObject(stateField)) {
      return true;
    }
    final CollectionMutation collectionMutation = methodParameter.retrieveRelatedValue(COLLECTION_MUTATION, CollectionMutation::withName);
    return isValueObjectCollection(stateField) && collectionMutation.isSingleParameterBased();
  }

  public static boolean isEventFieldAssignableToValueObject(final CodeGenerationParameter eventField) {
    return isMethodParameterAssignableToValueObject(eventField, eventField);
  }
}
