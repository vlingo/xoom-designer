// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.csharp.formatting;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CollectionMutation;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.csharp.DataObjectDetail;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;
import io.vlingo.xoom.designer.codegen.csharp.ValueObjectDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.dialect.Dialect.*;
import static io.vlingo.xoom.designer.codegen.csharp.AggregateDetail.stateFieldWithName;
import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toCamelCase;
import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public class Member extends Formatters.Fields<List<String>> {

  protected String FIELD_MEMBER_INSTANTIATION = "%s {get;} = %s;";
  protected String FIELD_MEMBER_DECLARATION = "%s {get;}";
  private final BiFunction<String, String, String> declarationResolver;
  private final String valueObjectTypeSuffix;

  Member(final Dialect dialect) {
    this(dialect, "");
  }

  public Member(final Dialect dialect, final String valueObjectTypeSuffix) {
    if (!RESOLVERS.containsKey(dialect)) {
      throw new IllegalArgumentException("Unable to format members on " + dialect);
    }
    this.declarationResolver = RESOLVERS.get(dialect);
    this.valueObjectTypeSuffix = valueObjectTypeSuffix;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent, final Stream<CodeGenerationParameter> fields) {
    return fields.map(field -> {
      if (field.isLabeled(Label.VALUE_OBJECT_FIELD) || field.hasAny(Label.FIELD_TYPE)) {
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
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);

    final CollectionMutation collectionMutation =
        field.retrieveRelatedValue(Label.COLLECTION_MUTATION, CollectionMutation::withName);

    if (!collectionMutation.isSingleParameterBased() && FieldDetail.isCollection(field)) {
      final String collectionType = FieldDetail.resolveCollectionType(field);
      if(DataObjectDetail.isValidSuffix(valueObjectTypeSuffix))  {
        return DataObjectDetail.adaptCollectionGenericsType(collectionType);
      }
      return collectionType;
    }
    
    if(FieldDetail.isScalar(field))
      return toCamelCase(fieldType);

    if (ValueObjectDetail.isValueObject(field))
      return toPascalCase(fieldType) + valueObjectTypeSuffix;

    return toPascalCase(fieldType);
  }

  private String resolveInstantiation(final CodeGenerationParameter field) {
    if (FieldDetail.requireImmediateInstantiation(field)) {
      final String defaultValue = FieldDetail.resolveDefaultValue(field.parent(), field.value);
      if(FieldDetail.isValueObjectCollection(field)) {
        final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
        return String.format(FIELD_MEMBER_INSTANTIATION, toPascalCase(field.value), defaultValue.replace(fieldType, fieldType + valueObjectTypeSuffix));
      }
      return String.format(FIELD_MEMBER_INSTANTIATION, toPascalCase(field.value), defaultValue);
    }
    final String instanceName = field.hasAny(Label.ALIAS) ? field.retrieveRelatedValue(Label.ALIAS) : field.value;
    return String.format(FIELD_MEMBER_DECLARATION, toPascalCase(instanceName));
  }

  private static final Map<Dialect, BiFunction<String, String, String>> RESOLVERS =
      new HashMap<Dialect, BiFunction<String, String, String>>() {{
        put(JAVA, (fieldType, fieldName) -> String.format("public final %s %s;", fieldType, fieldName));
        put(KOTLIN, (fieldType, fieldName) -> String.format("val %s: %s;", fieldName, fieldType));
        put(C_SHARP, (fieldType, fieldName) -> String.format("public %s %s", fieldType, fieldName));
      }};

}
