// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.parameters.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CollectionMutation {

  NONE("", param -> Collections.emptyList()),
  REPLACEMENT("*", param -> Collections.emptyList()),
  ADDITION("+", param -> Arrays.asList(String.format("%s.add(%s);", param.stateFieldName(), param.name()))),
  REMOVAL("-", param -> Arrays.asList(String.format("%s.remove(%s);", param.stateFieldName(), param.name()))),
  MERGE("#", param -> Arrays.asList(String.format("%s.removeAll(%s);", param.stateFieldName(), param.stateFieldName()),
          String.format("%s.addAll(%s);", param.stateFieldName(), param.stateFieldName())));

  private final String symbol;
  private final Function<MethodParameter, List<String>> statementsResolver;

  CollectionMutation(final String symbol,
                     final Function<MethodParameter, List<String>> statementsResolver) {
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

  public List<String> resolveStatements(final String collectionOwner, final MethodParameter methodParameter) {
    return statementsResolver.apply(methodParameter).stream().map(statement -> collectionOwner + "." + statement).collect(Collectors.toList());
  }

  public boolean shouldReplaceWithMethodParameter() {
    return equals(REPLACEMENT) || equals(NONE);
  }
}
