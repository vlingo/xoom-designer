// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

import java.util.List;

public interface Type {

  static Type of(final String typeName) {
    return with(typeName, null);
  }

  static Type with(final String typeName, final String collectionTypeName) {
    if(CollectionType.isCollection(collectionTypeName)) {
      return CollectionType.of(collectionTypeName, typeName);
    }
    if(ScalarType.isScalar(typeName)) {
      return ScalarType.withName(typeName);
    }
    if(DateType.isDate(typeName)) {
      return new DateType();
    }
    if(DateTimeType.isDateTime(typeName)) {
      return new DateTimeType();
    }
    return ValueObjectType.of(typeName);
  }

  String typeName();

  String defaultValue();

  List<String> qualifiedClassNames();

  default String typeNameWithParameterizedGenericType() {
    throw new UnsupportedOperationException("Parameterized generic type is not supported by default.");
  }

  default String parameterizedGenericType() {
    throw new UnsupportedOperationException("Parameterized generic type is not supported by default.");
  }

  default String qualifiedClassName() {
    return qualifiedClassNames().get(0);
  }

  default boolean isReferencableByQualifiedClassName() {
    return qualifiedClassNames().isEmpty();
  }

  default boolean isNumeric() {
    return false;
  }

  default boolean isScalar() {
    return false;
  }

  default boolean isString() {
    return false;
  }

  default boolean isValueObject() {
    return false;
  }

  default boolean isCollection() {
    return false;
  }

  default boolean isValueObjectCollection() {
    return false;
  }

}
