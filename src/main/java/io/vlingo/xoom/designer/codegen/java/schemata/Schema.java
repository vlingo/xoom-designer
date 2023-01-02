// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.codegen.content.CodeElementFormatter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.CodeGenerationProperties;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;
import io.vlingo.xoom.turbo.ComponentRegistry;

import java.util.function.Function;

import static io.vlingo.xoom.designer.codegen.java.model.FieldDetail.isDateTime;

public class Schema {

  public final String file;
  public final String reference;
  private static final Function<String, String> schemaFieldTypeConverter = t -> isDateTime(t) ? "timestamp" : t;

  public Schema(final String schemaReference) {
    this.reference = schemaReference;
    this.file = null;
  }

  public Schema(final String schemaGroup,
                final CodeGenerationParameter publishedLanguage) {
    if (!(publishedLanguage.isLabeled(Label.DOMAIN_EVENT) || publishedLanguage.isLabeled(Label.VALUE_OBJECT))) {
      throw new IllegalArgumentException("A Domain Event or Value Object parameter is expected.");
    }
    this.reference = String.format("%s:%s:%s", schemaGroup, publishedLanguage.value, CodeGenerationProperties.DEFAULT_SCHEMA_VERSION);
    this.file = publishedLanguage.value + ".vss";
  }

  public String simpleClassName() {
    return reference.split(":")[3];
  }

  public String qualifiedName() {
    final CodeElementFormatter formatter = ComponentRegistry.withName("defaultCodeFormatter");
    final String packageName =  reference.split(":")[2] + ".event";
    return formatter.qualifiedNameOf(packageName, simpleClassName());
  }

  public String innerReceiverClassName() {
    return simpleClassName() + "Receiver";
  }

  static String resolveFieldDeclaration(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);

    if (FieldDetail.isAssignableToValueObject(field)) {
      return String.format("data.%s:%s %s", fieldType, CodeGenerationProperties.DEFAULT_SCHEMA_VERSION, field.value);
    }

    if (FieldDetail.isCollection(field)) {
      final String convertedGenericType = schemaFieldTypeConverter.apply(FieldDetail.genericTypeOf(field.parent(), field.value));
      if (FieldDetail.isScalar(convertedGenericType)) {
        return String.format("%s[] %s", convertedGenericType.toLowerCase(), field.value);
      } else {
        return String.format("data.%s:%s[] %s", schemaFieldTypeConverter.apply(convertedGenericType), CodeGenerationProperties.DEFAULT_SCHEMA_VERSION, field.value);
      }
    }

    return schemaFieldTypeConverter.apply(fieldType).toLowerCase() + " " + field.value;
  }

}
