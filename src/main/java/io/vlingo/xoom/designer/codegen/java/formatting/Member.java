// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.formatting;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.designer.codegen.java.model.valueobject.ValueObjectDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.dialect.Dialect.JAVA;
import static io.vlingo.xoom.codegen.dialect.Dialect.KOTLIN;
import static io.vlingo.xoom.designer.codegen.java.model.aggregate.AggregateDetail.stateFieldWithName;

public class Member extends Formatters.Fields<List<String>> {

  private final String valueObjectTypeSuffix;
  private final BiFunction<String, String, String> declarationResolver;

  Member(final Dialect dialect) {
    this(dialect, "");
  }

  Member(final Dialect dialect, final String valueObjectTypeSuffix) {
    if (!RESOLVERS.containsKey(dialect)) {
      throw new IllegalArgumentException("Unable to format members on " + dialect);
    }
    this.declarationResolver = RESOLVERS.get(dialect);
    this.valueObjectTypeSuffix = valueObjectTypeSuffix;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent,
                             final Stream<CodeGenerationParameter> fields) {
    return fields.map(field -> {
      if(field.isLabeled(Label.VALUE_OBJECT_FIELD) || field.hasAny(Label.FIELD_TYPE)) {
        return field;
      }
      return stateFieldWithName(field.parent(Label.AGGREGATE), field.value);
    }).map(field -> {
      final String fieldType = resolveFieldType(field);
      final String fieldInstantiation = resolveInstantiation(field);
      return declarationResolver.apply(fieldType, fieldInstantiation);
    }).collect(Collectors.toList());
  }

  private String resolveFieldType(final CodeGenerationParameter field) {
    final String fieldType =
            field.retrieveRelatedValue(Label.FIELD_TYPE);

    final CollectionMutation collectionMutation =
            field.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

    if (!collectionMutation.isSingleParameterBased() && FieldDetail.isCollection(field)) {
      final String collectionType = FieldDetail.resolveCollectionType(field);
      if(DataObjectDetail.isValidSuffix(valueObjectTypeSuffix))  {
        return DataObjectDetail.adaptCollectionGenericsType(collectionType);
      }
      return collectionType;
    }

    if (ValueObjectDetail.isValueObject(field)) {
      return fieldType + valueObjectTypeSuffix;
    }
    return fieldType;
  }

  private String resolveInstantiation(final CodeGenerationParameter field) {
    if (FieldDetail.requireImmediateInstantiation(field)) {
      return String.format("%s = %s", field.value, FieldDetail.resolveDefaultValue(field.parent(), field.value));
    }
    return field.hasAny(Label.ALIAS) ? field.retrieveRelatedValue(Label.ALIAS) : field.value;
  }

  @SuppressWarnings("serial")
  private static final Map<Dialect, BiFunction<String, String, String>> RESOLVERS =
          new HashMap<Dialect, BiFunction<String, String, String>>() {{
            put(JAVA, (fieldType, fieldName) -> String.format("public final %s %s;", fieldType, fieldName));
            put(KOTLIN, (fieldType, fieldName) -> String.format("val %s: %s;", fieldName, fieldType));
          }};

}