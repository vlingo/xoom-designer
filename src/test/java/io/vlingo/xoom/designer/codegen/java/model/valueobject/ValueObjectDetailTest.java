// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.model.valueobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueObjectDetailTest {

  @Test
  public void testThatValueObjectsAreSorted() {
    final CodeGenerationParameter name =
            CodeGenerationParameter.of(Label.VALUE_OBJECT, "Name")
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "firstName")
                            .relate(Label.FIELD_TYPE, "String"))
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "lastName")
                            .relate(Label.FIELD_TYPE, "String"));

    final CodeGenerationParameter score =
            CodeGenerationParameter.of(Label.VALUE_OBJECT, "Score")
                    .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "value")
                            .relate(Label.FIELD_TYPE, "long"));

    final CodeGenerationParameter classification =
            CodeGenerationParameter.of(Label.VALUE_OBJECT, "Classification")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "name")
                        .relate(Label.FIELD_TYPE, "String"))
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "score")
                        .relate(Label.FIELD_TYPE, "Score"));

    final CodeGenerationParameter rank =
            CodeGenerationParameter.of(Label.VALUE_OBJECT, "Rank")
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "description")
                        .relate(Label.FIELD_TYPE, "String"))
                .relate(CodeGenerationParameter.of(Label.VALUE_OBJECT_FIELD, "classification")
                        .relate(Label.FIELD_TYPE, "Classification"));

    final List<CodeGenerationParameter> sorted =
            ValueObjectDetail.orderByDependency(Stream.of(rank, name, classification, score))
                    .collect(Collectors.toList());

    Assertions.assertEquals("Score", sorted.get(0).value);
    Assertions.assertEquals("Classification", sorted.get(1).value);
    Assertions.assertEquals("Rank", sorted.get(2).value);
    Assertions.assertEquals("Name", sorted.get(3).value);
  }
}
