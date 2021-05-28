// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java.unittest;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.task.projectgeneration.Label;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.formatting.NumberFormat;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.FieldDetail;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.model.valueobject.ValueObjectDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class TestDataValueGenerator {

  private final int dataSetSize;
  private final String valuePrefix;
  @SuppressWarnings("unused")
  private final String aggregateName;
  private final TestDataValues generatedValues = new TestDataValues();
  private final List<CodeGenerationParameter> stateFields = new ArrayList<>();
  private final List<CodeGenerationParameter> valueObjects = new ArrayList<>();
  private boolean currentBooleanValue;
  private int currentNumericValue;
  private char currentCharValue;

  public static TestDataValueGenerator with(final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> valueObjects) {
    return with(1, "", aggregate, valueObjects);
  }

  public static TestDataValueGenerator with(final int dataSetSize,
                                            final String valuePrefix,
                                            final CodeGenerationParameter aggregate,
                                            final List<CodeGenerationParameter> valueObjects) {
    final List<CodeGenerationParameter> stateFields =
            aggregate.retrieveAllRelated(Label.STATE_FIELD).collect(toList());

    return with(dataSetSize, valuePrefix, aggregate.value, stateFields, valueObjects);
  }

  public static TestDataValueGenerator with(final int dataSetSize,
                                            final String valuePrefix,
                                            final String aggregateName,
                                            final List<CodeGenerationParameter> stateFields,
                                            final List<CodeGenerationParameter> valueObjects) {
    return new TestDataValueGenerator(dataSetSize, aggregateName, valuePrefix, stateFields, valueObjects);
  }

  private TestDataValueGenerator(final int dataSetSize,
                                 final String valuePrefix,
                                 final String aggregateName,
                                 final List<CodeGenerationParameter> stateFields,
                                 final List<CodeGenerationParameter> valueObjects) {

    this.dataSetSize = dataSetSize;
    this.valuePrefix = valuePrefix;
    this.aggregateName = aggregateName;
    this.stateFields.addAll(stateFields);
    this.valueObjects.addAll(valueObjects);
  }

  public TestDataValues generate() {
    IntStream.range(1, dataSetSize + 1).forEach(this::generateValues);
    return generatedValues;
  }

  private void generateValues(final int dataIndex) {
    resetCurrentValues();
    stateFields.forEach(field -> this.generateValue(dataIndex, "", field));
  }

  private void generateValue(final int dataIndex, final String path, final CodeGenerationParameter field) {
    if (ValueObjectDetail.isValueObject(field) || FieldDetail.isValueObjectCollection(field)) {
      generateForValueObjectFields(dataIndex, path, field);
    } else if(FieldDetail.isScalar(field) || FieldDetail.isCollection(field)) {
      generateScalarFieldAssignment(dataIndex, path, field);
    }
  }

  private void generateForValueObjectFields(final int dataIndex, final String path, final CodeGenerationParameter field) {
    final String fieldType =
            field.retrieveRelatedValue(Label.FIELD_TYPE);

    final CodeGenerationParameter valueObject =
            ValueObjectDetail.valueObjectOf(fieldType, valueObjects.stream());

    final String currentPath = resolvePath(path, field);

    final Consumer<CodeGenerationParameter> valueObjectFieldAssignment =
            valueObjectField -> generateValue(dataIndex, currentPath, valueObjectField);

    valueObject.retrieveAllRelated(Label.VALUE_OBJECT_FIELD).forEach(valueObjectFieldAssignment);
  }

  private void generateScalarFieldAssignment(final int dataIndex, final String path, final CodeGenerationParameter field) {
    final String currentPath = resolvePath(path, field);
    final String fieldType = field.retrieveRelatedValue(Label.FIELD_TYPE);
    if (field.value.equalsIgnoreCase("id")) {
      generatedValues.add(dataIndex, fieldType, currentPath, quoteValue(dataIndex));
    } else if (FieldDetail.hasNumericType(field)) {
      generatedValues.add(dataIndex, fieldType, currentPath, currentNumericValue);
      alternateNumericValue();
    } else if (FieldDetail.hasBooleanType(field)) {
      generatedValues.add(dataIndex, fieldType, currentPath, currentBooleanValue);
      alternateBooleanValue();
    } else if (FieldDetail.hasCharType(field)) {
      generatedValues.add(dataIndex, fieldType, currentPath, quoteValue(currentCharValue));
      alternateCharValue();
    } else if (FieldDetail.hasStringType(field)) {
      final String alias = valuePrefix.toLowerCase();
      final String ordinalIndex = NumberFormat.toOrdinal(dataIndex);
      final String hyphenatedPath = currentPath.replaceAll("\\.", "-");
      final String value = formatStringValue(ordinalIndex, alias, hyphenatedPath);
      generatedValues.add(dataIndex, fieldType, currentPath, quoteValue(value));
    } else {
      throw new IllegalArgumentException(field.value + " " + field.retrieveRelatedValue(Label.FIELD_TYPE) + " is not Scalar");
    }
  }

  private String formatStringValue(final String ordinalIndex, final String alias, final String hyphenatedPath) {
    final StringBuilder value = new StringBuilder(hyphenatedPath);
    if (!alias.isEmpty()) {
      value.insert(0, alias.concat("-"));
    }
    if (dataSetSize > 1) {
      value.insert(0, ordinalIndex.concat("-"));
    }
    return value.toString();
  }

  private String quoteValue(final Object value) {
    final String quote = Character.class.equals(value.getClass()) ? "'" : "\"";
    return quote + value + quote;
  }

  private String resolvePath(final String path, final CodeGenerationParameter field) {
    return path.isEmpty() ? field.value : path + "." + field.value;
  }

  private void alternateNumericValue() {
    this.currentNumericValue++;
  }

  private void alternateBooleanValue() {
    this.currentBooleanValue = !currentBooleanValue;
  }

  private void alternateCharValue() {
    this.currentCharValue = currentCharValue++;
  }

  private void resetCurrentValues() {
    this.currentNumericValue = 1;
    this.currentBooleanValue = true;
    this.currentCharValue = '1';
  }

  public static class TestDataValues {

    private final Map<Integer, List<TestDataValue>> generatedValues = new HashMap<>();

    public TestDataValues updateAllValues() {
      final TestDataValues updated = new TestDataValues();
      generatedValues.forEach((index, values) -> {
        values.forEach(value -> {
          updated.add(index, value.updateValue());
        });
      });
      return updated;
    }

    public void add(final int dataIndex, final String type, final String path, final Object value) {
      add(dataIndex, new TestDataValue(type, path, value.toString()));
    }

    private void add(final int dataIndex, final TestDataValue newValue) {
      generatedValues.computeIfAbsent(dataIndex, v -> new ArrayList<>()).add(newValue);
    }

    public String retrieve(final String path) {
      return retrieve(1, path);
    }

    public String retrieve(final String variableName, final String path) {
      return retrieve(1, variableName, path);
    }

    public String retrieve(final int dataIndex, final String variableName, final String path) {
      final String reducedPath =
              path.substring(variableName.length() + 1);

      return retrieve(dataIndex, reducedPath);
    }

    public String retrieve(final int dataIndex, final String path) {
      return generatedValues.get(dataIndex).stream().filter(value -> value.hasPath(path))
              .findFirst().orElseThrow(() -> new IllegalArgumentException("Unable to find value for " + path)).value;
    }

    private class TestDataValue {
      public final String fieldPath;
      public final String value;
      public final String type;

      private TestDataValue(final String type, final String fieldPath, final String value) {
        this.fieldPath = fieldPath;
        this.value = value;
        this.type = type;
      }

      public boolean hasPath(final String fieldPath) {
        return this.fieldPath.equals(fieldPath);
      }

      public TestDataValue updateValue() {
        final String value = resolveUpdatedValue();
        return new TestDataValue(type, fieldPath, value);
      }

      private String resolveUpdatedValue() {
        if (FieldDetail.isNumeric(type)) {
          return String.valueOf(Integer.valueOf(value) + 1);
        }
        if (FieldDetail.isBoolean(type)) {
          return String.valueOf(!Boolean.valueOf(value));
        }
        return value.replaceFirst("\"", "\"updated-");
      }
    }
  }
}
