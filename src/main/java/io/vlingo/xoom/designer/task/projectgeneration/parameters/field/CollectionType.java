// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.parameters.field;

import java.util.Arrays;
import java.util.List;

public class CollectionType implements Type {

  private final String name;
  private final Type parameterizedGenericType;

  private static final String SetName = "Set";
  private static final String ListName = "List";

  public static CollectionType of(final String parameterizedGenericType,
                                  final String collectionType) {
    return new CollectionType(collectionType, parameterizedGenericType);
  }

  private CollectionType (final String collectionType,
                          final String parameterizedGenericType) {
    this.name = collectionType;
    this.parameterizedGenericType = Type.of(parameterizedGenericType);
  }

  @Override
  public String typeName() {
    return name;
  }

  @Override
  public String typeNameWithParameterizedGenericType() {
    return String.format("%s<%s>", name, parameterizedGenericType);
  }

  @Override
  public String parameterizedGenericType() {
    return parameterizedGenericType.typeName();
  }

  @Override
  public String defaultValue() {
    if(name.equals(ListName)) {
      return "new java.util.ArrayList()";
    } else {
      return "new java.util.HashSet()";
    }
  }

  @Override
  public List<String> qualifiedClassNames() {
    final String qualifiedCollectionName = "java.util." + name;
    if(parameterizedGenericType.isReferencableByQualifiedClassName()) {
      return Arrays.asList(qualifiedCollectionName, parameterizedGenericType.qualifiedClassName());
    }
    return Arrays.asList(qualifiedCollectionName);
  }

  @Override
  public boolean isCollection() {
    return true;
  }

  @Override
  public boolean isValueObjectCollection() {
    return parameterizedGenericType.isValueObject();
  }

  public static boolean isCollection(final String typeName) {
    return typeName != null && (typeName.equals(ListName) || typeName.equals(SetName));
  }
}
