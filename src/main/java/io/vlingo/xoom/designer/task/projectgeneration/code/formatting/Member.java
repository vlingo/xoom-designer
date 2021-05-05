// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.formatting;

import io.vlingo.xoom.designer.task.projectgeneration.code.template.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.template.model.aggregate.AggregateDetail;
import io.vlingo.xoom.turbo.codegen.language.Language;
import io.vlingo.xoom.turbo.codegen.parameter.CodeGenerationParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.turbo.codegen.language.Language.JAVA;
import static io.vlingo.xoom.turbo.codegen.language.Language.KOTLIN;

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
      if (!field.hasAny(Label.FIELD_TYPE)) {
        return AggregateDetail.stateFieldWithName(field.parent(Label.AGGREGATE), field.value);
      }
      return field;
    }).map(field -> {
      final String fieldType = resolveFieldType(field);
      return declarationResolver.apply(fieldType, field.value);
    }).collect(Collectors.toList());
  }

  private String resolveFieldType(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);

    if (FieldDetail.isScalar(field)) {
      return fieldType;
    }
    return fieldType + valueObjectTypeSuffix;
  }

  @SuppressWarnings("serial")
  private static final Map<Language, BiFunction<String, String, String>> RESOLVERS =
          new HashMap<Language, BiFunction<String, String, String>>() {{
            put(JAVA, (fieldType, fieldName) -> String.format("public final %s %s;", fieldType, fieldName));
            put(KOTLIN, (fieldType, fieldName) -> String.format("val %s: %s;", fieldName, fieldType));
          }};

}