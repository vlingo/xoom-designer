// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;

import static io.vlingo.xoom.designer.task.projectgeneration.CodeGenerationProperties.DEFAULT_SCHEMA_VERSION;

public class Schema {

  public final String reference;
  public final String file;

  public Schema(final CodeGenerationParameter schema) {
    if (!schema.isLabeled(Label.SCHEMA)) {
      throw new IllegalArgumentException("A schema parameter is expected.");
    }
    this.reference = schema.value;
    this.file = null;
  }

  public Schema(final String schemaGroup,
                final CodeGenerationParameter publishedLanguage) {
    if (!(publishedLanguage.isLabeled(Label.DOMAIN_EVENT) || publishedLanguage.isLabeled(Label.VALUE_OBJECT))) {
      throw new IllegalArgumentException("A Domain Event or Value Object parameter is expected.");
    }
    this.reference = String.format("%s:%s:%s", schemaGroup, publishedLanguage.value, DEFAULT_SCHEMA_VERSION);
    this.file = publishedLanguage.value + ".vss";
  }

  static String resolveFieldDeclaration(final CodeGenerationParameter field) {
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);

    if (FieldDetail.isAssignableToValueObject(field)) {
      return String.format("Data.%s:%s %s", fieldType, DEFAULT_SCHEMA_VERSION, field.value);
    }

    if (FieldDetail.isCollection(field)) {
      final String genericType = FieldDetail.genericTypeOf(field.parent(), field.value);
      if (FieldDetail.isScalar(genericType)) {
        return String.format("%s[] %s", genericType, field.value);
      } else {
        return String.format("Data.%s:%s[] %s", genericType, DEFAULT_SCHEMA_VERSION, field.value);
      }
    }
    return fieldType.toLowerCase() + " " + field.value;
  }
}
