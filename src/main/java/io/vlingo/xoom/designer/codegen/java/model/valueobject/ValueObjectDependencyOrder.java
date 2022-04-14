// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.java.model.valueobject;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.Label;
import io.vlingo.xoom.designer.codegen.java.model.FieldDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueObjectDependencyOrder {

  private final List<CodeGenerationParameter> valueObjects = new ArrayList<>();
  private final List<CodeGenerationParameter> sortedValueObjects = new ArrayList<>();

  protected static Stream<CodeGenerationParameter> sort(final CodeGenerationParameter... valueObjects) {
    return sort(Stream.of(valueObjects));
  }

  protected static Stream<CodeGenerationParameter> sort(final Stream<CodeGenerationParameter> valueObjects) {
    return new ValueObjectDependencyOrder(valueObjects.collect(Collectors.toList())).sort();
  }

  private ValueObjectDependencyOrder(final List<CodeGenerationParameter> valueObjects) {
    this.valueObjects.clear();
    this.valueObjects.addAll(valueObjects);
  }

  private Stream<CodeGenerationParameter> sort() {
    valueObjects.stream().forEach(this::handleDependency);
    return sortedValueObjects.stream();
  }

  private void handleDependency(final CodeGenerationParameter valueObject) {
    if (!isSorted(valueObject)) {
      final List<CodeGenerationParameter> dependencies = findDependencies(valueObject);
      if (!dependencies.isEmpty()) {
        dependencies.forEach(this::handleDependency);
      }
      sortedValueObjects.add(valueObject);
    }
  }

  private List<CodeGenerationParameter> findDependencies(final CodeGenerationParameter valueObject) {
    return valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD)
            .filter(field -> ValueObjectDetail.isValueObject(field) || FieldDetail.isValueObjectCollection(field))
            .map(field -> field.retrieveRelatedValue(Label.FIELD_TYPE))
            .map(type -> ValueObjectDetail.valueObjectOf(type, valueObjects.stream()))
            .collect(Collectors.toList());
  }

  private boolean isSorted(final CodeGenerationParameter valueObject) {
    return sortedValueObjects.stream().anyMatch(existing -> existing.value.equals(valueObject.value));
  }

}