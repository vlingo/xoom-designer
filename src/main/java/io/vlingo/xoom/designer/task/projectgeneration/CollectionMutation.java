// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.ALIAS;

public enum CollectionMutation {

  NONE("", param -> Collections.emptyList()),
  REPLACEMENT("*", param -> Collections.emptyList()),
  ADDITION("+", param -> Arrays.asList(String.format("this.%s.add(%s);", param.value, param.retrieveRelatedValue(ALIAS)))),
  REMOVAL("-", param -> Arrays.asList(String.format("this.%s.remove(%s);", param.value, param.retrieveRelatedValue(ALIAS)))),
  MERGE("#", param -> Arrays.asList(String.format("this.%s.removeAll(%s);", param.value, param.value),
          String.format("this.%s.addAll(%s);", param.value, param.value)));

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

  public List<String> resolveStatements(final CodeGenerationParameter methodParameter) {
    return statementsResolver.apply(methodParameter);
  }

  public boolean shouldReplaceWithMethodParameter() {
    return equals(REPLACEMENT) || equals(NONE);
  }
}
