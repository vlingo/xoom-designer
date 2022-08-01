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
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueObjectDetail {

  public static String resolvePackage(final String basePackage) {
    return String.format("%s.%s", basePackage, "Model");
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
      return Stream.of(codeElementFormatter.simpleNameOf(packageName)).collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  public static String resolveTypeImport(final String valueObjectName,
                                         final List<Content> contents) {
    return ContentQuery.findFullyQualifiedClassName(CsharpTemplateStandard.VALUE_OBJECT, valueObjectName, contents);
  }

  public static CodeGenerationParameter valueObjectOf(final String valueObjectType,
                                                      final Stream<CodeGenerationParameter> valueObjects) {
    return valueObjects.filter(valueObject -> valueObject.value.equals(valueObjectType)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to find " + valueObjectType));
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
}
