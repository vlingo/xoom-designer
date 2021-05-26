// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;

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
                final CodeGenerationParameter publishedDialect) {
    if (!(publishedDialect.isLabeled(Label.DOMAIN_EVENT) || publishedDialect.isLabeled(Label.VALUE_OBJECT))) {
      throw new IllegalArgumentException("A Domain Event or Value Object parameter is expected.");
    }
    this.reference = String.format("%s:%s:%s", schemaGroup, publishedDialect.value, DEFAULT_SCHEMA_VERSION);
    this.file = publishedDialect.value + ".vss";
  }

  public String getReference() {
    return reference;
  }

  public String getFile() {
    return file;
  }

}
