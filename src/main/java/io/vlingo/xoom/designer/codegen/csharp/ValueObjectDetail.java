// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toCamelCase;
import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class ValueObjectDetail {
  private static final String SINGLE_PARAMETER_COLLECTION_PATTERN = "%s.Select(%s.To%s)";
  private static final String MULTI_PARAMETERS_COLLECTION_PATTERN = "%s.Select(%s.to%s).To%s()";
  private static final String JOINING_STRING_ARGUMENTS_DELIMITER = ", ";

  public static String resolvePackage(final String basePackage) {
    return String.format("%s.%s", basePackage, "Model");
  }

  public static Set<String> resolveImports(final List<Content> contents, final Stream<CodeGenerationParameter> arguments) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("cSharpCodeFormatter");

    final Optional<String> anyQualifiedName = arguments
        .filter(field -> isValueObject(field) || FieldDetail.isValueObjectCollection(field))
        .map(arg -> arg.retrieveRelatedValue(Label.FIELD_TYPE))
        .map(valueObjectName -> resolveTypeImport(valueObjectName, contents))
        .findAny();

    if (anyQualifiedName.isPresent()) {
      final String packageName = codeElementFormatter.packageOf(anyQualifiedName.get());
      return Stream.of(codeElementFormatter.packageOf(packageName)).collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  public static String resolveTypeImport(final String valueObjectName, final List<Content> contents) {
    return ContentQuery.findFullyQualifiedClassName(CsharpTemplateStandard.VALUE_OBJECT, valueObjectName, contents);
  }

  public static CodeGenerationParameter valueObjectOf(final String valueObjectType,
                                                      final Stream<CodeGenerationParameter> valueObjects) {
    return valueObjects.filter(valueObject -> valueObject.value.equals(valueObjectType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unable to find " + valueObjectType));
  }

  public static boolean isValueObject(final CodeGenerationParameter field) {
    return !FieldDetail.isScalar(field) && !FieldDetail.isCollection(field) && !FieldDetail.isDateTime(field);
  }

  public static Set<String> resolveFieldsImports(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .map(FieldDetail::resolveImportForType)
        .collect(Collectors.toSet());
  }

  public static boolean useValueObject(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD)
        .anyMatch(field -> FieldDetail.isValueObjectCollection(field) || ValueObjectDetail.isValueObject(field));
  }

  public static List<String> retrieveAllMemberNames(CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .map(p -> toPascalCase(p.value))
        .collect(Collectors.toList());
  }

  public static CodeGenerationParameter valueObjectFieldWithName(final CodeGenerationParameter parent, final String fieldName) {
    return parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .filter(field -> field.value.equals(fieldName))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to find " + fieldName));
  }

  public static String translateDataObjectCollection(final String fieldPath, final CodeGenerationParameter valueObjectField) {
    return translateDataObjectCollection(fieldPath, valueObjectField, null);
  }

  public static String translateDataObjectCollection(final String fieldPath, final CodeGenerationParameter valueObjectField,
                                                     final CodeGenerationParameter methodParameter) {
    final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
    final String collectionType = valueObjectField.retrieveRelatedValue(Label.COLLECTION_TYPE);
    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);

    if (methodParameter != null) {
      final CollectionMutation collectionMutation =
          methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

      if (collectionMutation.isSingleParameterBased()) {
        return String.format(SINGLE_PARAMETER_COLLECTION_PATTERN, fieldPath, dataObjectName, fieldType);
      }
    }
    if(FieldDetail.isSetTypedCollection(valueObjectField))
      return String.format(MULTI_PARAMETERS_COLLECTION_PATTERN, fieldPath, dataObjectName, fieldType, "Hash" + collectionType);
    return String.format(MULTI_PARAMETERS_COLLECTION_PATTERN, fieldPath, dataObjectName, fieldType, collectionType);
  }

  public static String resolveEmptyObjectArguments(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .map(field -> FieldDetail.resolveDefaultValue(field.parent(), field.value))
        .collect(Collectors.joining(JOINING_STRING_ARGUMENTS_DELIMITER));
  }

  public static List<String> resolveFieldsNames(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .map(p -> toPascalCase(p.value))
        .collect(Collectors.toList());
  }

  public static String joinValueObjectFields(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
        .map(ValueObjectDetail::translateDataObjectField)
        .collect(Collectors.joining(JOINING_STRING_ARGUMENTS_DELIMITER));
  }

  private static String translateDataObjectField(final CodeGenerationParameter field) {
    if (FieldDetail.isValueObjectCollection(field))
      return ValueObjectDetail.translateDataObjectCollection(toPascalCase(field.value), field);
    if (ValueObjectDetail.isValueObject(field))
      return toCamelCase(field.value);

    return toPascalCase(field.value);
  }
}
