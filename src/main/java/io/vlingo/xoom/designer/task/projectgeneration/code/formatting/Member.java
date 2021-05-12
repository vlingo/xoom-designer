// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.codegen.language.Language;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.valueobject.ValueObjectDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.language.Language.JAVA;
import static io.vlingo.xoom.codegen.language.Language.KOTLIN;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.AGGREGATE;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.Label.VALUE_OBJECT_FIELD;
import static io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail.stateFieldWithName;

public class Member extends Formatters.Fields<List<String>> {

  private final String valueObjectTypeSuffix;
  private final BiFunction<String, String, String> declarationResolver;

  Member(final Language language) {
    this(language, "");
  }

  Member(final Language language, final String valueObjectTypeSuffix) {
    if (!RESOLVERS.containsKey(language)) {
      throw new IllegalArgumentException("Unable to format members on " + language);
    }
    this.declarationResolver = RESOLVERS.get(language);
    this.valueObjectTypeSuffix = valueObjectTypeSuffix;
  }

  @Override
  public List<String> format(final CodeGenerationParameter parent,
                             final Stream<CodeGenerationParameter> fields) {
    return fields.map(field -> {
      if(field.isLabeled(VALUE_OBJECT_FIELD)) {
        return field;
      }
      return stateFieldWithName(field.parent(AGGREGATE), field.value);
    }).map(field -> {
      final String fieldType = resolveFieldType(field);
      final String fieldInstantiation = resolveInstantiation(field);
      return declarationResolver.apply(fieldType, fieldInstantiation);
    }).collect(Collectors.toList());
  }

  private String resolveFieldType(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    if (FieldDetail.isCollection(field)) {
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
    return field.value;
  }

  @SuppressWarnings("serial")
  private static final Map<Language, BiFunction<String, String, String>> RESOLVERS =
          new HashMap<Language, BiFunction<String, String, String>>() {{
            put(JAVA, (fieldType, fieldName) -> String.format("public final %s %s;", fieldType, fieldName));
            put(KOTLIN, (fieldType, fieldName) -> String.format("val %s: %s;", fieldName, fieldType));
          }};

}