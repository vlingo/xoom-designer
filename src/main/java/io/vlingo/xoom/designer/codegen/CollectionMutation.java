// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CollectionMutation {

  NONE("", param -> Collections.emptyList()),
  REPLACEMENT("*", param -> Collections.emptyList()),
  ADDITION("+", param -> Arrays.asList(String.format("%s.add(%s);", param.value, param.retrieveRelatedValue(Label.ALIAS)))),
  REMOVAL("-", param -> Arrays.asList(String.format("%s.remove(%s);", param.value, param.retrieveRelatedValue(Label.ALIAS)))),
  MERGE("#", param -> Arrays.asList(String.format("%s.removeAll(%s);", param.value, param.value),
          String.format("%s.addAll(%s);", param.value, param.value)));

  private final String symbol;
  private final Function<CodeGenerationParameter, List<String>> statementsResolver;

  CollectionMutation(final String symbol,
                     final Function<CodeGenerationParameter, List<String>> statementsResolver) {
    this.symbol = symbol;
    this.statementsResolver = statementsResolver;
  }

  public static CollectionMutation withSymbol(final String symbol) {
    return Stream.of(values()).filter(cm -> cm.symbol.equals(symbol)).findFirst().orElse(NONE);
  }

  public static CollectionMutation withName(final String name) {
    return Stream.of(values()).filter(cm -> cm.name().equals(name)).findFirst().orElse(NONE);
  }

  public boolean isSingleParameterBased() {
    return equals(ADDITION) || equals(REMOVAL);
  }

  public List<String> resolveStatements(final String collectionOwner, final CodeGenerationParameter methodParameter) {
    return statementsResolver.apply(methodParameter).stream().map(statement -> collectionOwner + "." + statement).collect(Collectors.toList());
  }

  public List<String> resolveStatements(final String collectionOwner, final String elementOwner, final CodeGenerationParameter methodParameter) {
    return resolveStatements(collectionOwner, methodParameter)
            .stream().map(statement -> statement.replace("(", "(" + elementOwner + "."))
            .collect(Collectors.toList());
  }

  public boolean shouldReplaceWithMethodParameter() {
    return equals(REPLACEMENT) || equals(NONE);
  }
}
