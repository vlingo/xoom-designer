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
import io.vlingo.xoom.designer.codegen.csharp.model.valueobject.ValueObjectDependencyOrder;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueObjectDetail {

  private static final String SINGLE_PARAMETER_COLLECTION_PATTERN = "%s.stream().map(%s::to%s).findFirst().orElse(null)";

  public static String resolvePackage(final String basePackage) {
    return String.format("%s.%s", basePackage, "model");
  }

  public static Set<String> resolveImports(final List<Content> contents,
                                           final Stream<CodeGenerationParameter> arguments) {
    final CodeElementFormatter codeElementFormatter = ComponentRegistry.withName("csharpCodeFormatter");

    final Optional<String> anyQualifiedName =
            arguments.filter(field -> ValueObjectDetail.isValueObject(field) || FieldDetail.isValueObjectCollection(field))
                    .map(arg -> arg.retrieveRelatedValue(Label.FIELD_TYPE))
                    .map(valueObjectName -> resolveTypeImport(valueObjectName, contents))
                    .findAny();

    if (anyQualifiedName.isPresent()) {
      final String packageName = codeElementFormatter.packageOf(anyQualifiedName.get());
      return Stream.of(codeElementFormatter.importAllFrom(packageName)).collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  public static String resolveTypeImport(final String valueObjectName,
                                         final List<Content> contents) {
    return ContentQuery.findFullyQualifiedClassName(CsharpTemplateStandard.VALUE_OBJECT, valueObjectName, contents);
  }

  public static Stream<CodeGenerationParameter> orderByDependency(final Stream<CodeGenerationParameter> valueObjects) {
    return ValueObjectDependencyOrder.sort(valueObjects);
  }

  public static Stream<CodeGenerationParameter> findPublishedValueObjects(final List<CodeGenerationParameter> exchanges,
                                                                          final List<CodeGenerationParameter> valueObjects) {
    final Map<String, CodeGenerationParameter> publishedValueObjects = new HashMap<>();

    final List<CodeGenerationParameter> topLevelValueObjects =
            exchanges.stream().filter(exchange -> exchange.hasAny(Label.DOMAIN_EVENT))
                    .flatMap(event -> event.retrieveAllRelated(Label.DOMAIN_EVENT))
                    .map(event -> AggregateDetail.eventWithName(event.parent(Label.AGGREGATE), event.value))
                    .flatMap(event -> event.retrieveAllRelated(Label.STATE_FIELD))
                    .map(stateField -> AggregateDetail.stateFieldWithName(stateField.parent(Label.AGGREGATE), stateField.value))
                    .filter(stateField-> ValueObjectDetail.isValueObject(stateField) || FieldDetail.isValueObjectCollection(stateField))
                    .map(field -> field.retrieveRelatedValue(Label.FIELD_TYPE))
                    .map(type -> valueObjectOf(type, valueObjects.stream()))
                    .collect(Collectors.toList());

    final List<CodeGenerationParameter> relatedValueObjects =
            topLevelValueObjects.stream().flatMap(vo -> collectRelatedValueObjects(vo, valueObjects))
                    .collect(Collectors.toList());

    Stream.of(topLevelValueObjects, relatedValueObjects).flatMap(List::stream).forEach(vo -> publishedValueObjects.putIfAbsent(vo.value, vo));

    return publishedValueObjects.values().stream();
  }

  public static CodeGenerationParameter valueObjectOf(final String valueObjectType,
                                                      final Stream<CodeGenerationParameter> valueObjects) {
    return valueObjects.filter(valueObject -> valueObject.value.equals(valueObjectType)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to find " + valueObjectType));
  }

  public static Stream<CodeGenerationParameter> collectRelatedValueObjects(final CodeGenerationParameter valueObject,
                                                                           final List<CodeGenerationParameter> valueObjects) {
    final List<CodeGenerationParameter> related = new ArrayList<>();
    findRelatedValueObjects(valueObject, valueObjects).forEach(vo -> collectRelatedValueObjects(vo, valueObjects, related));
    return related.stream();
  }

  private static void collectRelatedValueObjects(final CodeGenerationParameter valueObject,
                                                 final List<CodeGenerationParameter> valueObjects,
                                                 final List<CodeGenerationParameter> related) {
    if (related.stream().noneMatch(vo -> vo.value.equals(valueObject.value))) {
      related.add(valueObject);
    }
    findRelatedValueObjects(valueObject, valueObjects).forEach(vo -> collectRelatedValueObjects(vo, valueObjects, related));
  }

  private static Stream<CodeGenerationParameter> findRelatedValueObjects(final CodeGenerationParameter valueObject,
                                                                         final List<CodeGenerationParameter> valueObjects) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .filter(stateField-> ValueObjectDetail.isValueObject(stateField) || FieldDetail.isValueObjectCollection(stateField))
            .map(valueObjectField -> valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE))
            .map(valueObjectType -> valueObjectOf(valueObjectType, valueObjects.stream()));
  }

  public static boolean useValueObject(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(Label.STATE_FIELD)
            .anyMatch(field -> FieldDetail.isValueObjectCollection(field) || ValueObjectDetail.isValueObject(field));
  }


  public static boolean isValueObject(final CodeGenerationParameter field) {
    return !FieldDetail.isScalar(field) && !FieldDetail.isCollection(field) && !FieldDetail.isDateTime(field);
  }

  public static Set<String> resolveFieldsImports(final CodeGenerationParameter valueObject) {
    final Set<String> imports = new HashSet<>();
    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(field -> {
      imports.add(FieldDetail.resolveImportForType(field));
    });
    return imports;
  }

  public static CodeGenerationParameter valueObjectFieldWithName(final CodeGenerationParameter parent,
                                                                 final String fieldName) {
    return parent.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .filter(field -> field.value.equals(fieldName))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to find " + fieldName));
  }

  public static String translateDataObjectCollection(final String fieldPath,
                                                     final CodeGenerationParameter valueObjectField) {
    return translateDataObjectCollection(fieldPath, valueObjectField, null);
  }

  public static String translateDataObjectCollection(final String fieldPath,
                                                     final CodeGenerationParameter valueObjectField,
                                                     final CodeGenerationParameter methodParameter) {
    final String fieldType = valueObjectField.retrieveRelatedValue(Label.FIELD_TYPE);
    final String collectionType = valueObjectField.retrieveRelatedValue(Label.COLLECTION_TYPE);
    final String dataObjectName = CsharpTemplateStandard.DATA_OBJECT.resolveClassname(fieldType);

    if(methodParameter != null) {
      final CollectionMutation collectionMutation =
              methodParameter.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

      if(collectionMutation.isSingleParameterBased()) {
        return String.format(SINGLE_PARAMETER_COLLECTION_PATTERN, fieldPath, dataObjectName, fieldType);
      }
    }

    return String.format("%s.stream().map(%s::to%s).collect(java.util.stream.Collectors.to%s())",
            fieldPath, dataObjectName, fieldType, collectionType);
  }

}
