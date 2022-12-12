// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.designer.codegen.csharp.FieldDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.codegen.csharp.FieldDetail.toPascalCase;

public enum CollectionMutation {

  NONE("", (dialect, param) -> Collections.emptyList()),
  REPLACEMENT("*", (dialect, param) -> Collections.emptyList()),
  ADDITION("+", (dialect, param) -> Collections.singletonList(String.format("%s.%s(%s);", resolveMemberParamFrom(dialect, param.value), resolveAddMemberFrom(dialect), resolveMemberParamFrom(dialect, param)))),
  REMOVAL("-", (dialect, param) -> Collections.singletonList(String.format("%s.%s(%s);", resolveMemberParamFrom(dialect, param.value), resolveRemoveMemberFrom(dialect), resolveMemberParamFrom(dialect, param)))),
  MERGE("#", (dialect, param) -> Arrays.asList(String.format("%s.%s(%s);", resolveMemberParamFrom(dialect, param.value), resolveRemoveAllMemberFrom(dialect), resolveMemberParamFrom(dialect, param)),
      String.format("%s.%s(%s);", param.value, resolveAddAllMemberFrom(dialect), resolveMemberParamFrom(dialect, param))));

  private static String resolveMemberParamFrom(final Dialect dialect, final String param) {
    return dialect.isJava() ? param : toPascalCase(param);
  }

  private static String resolveMemberParamFrom(final Dialect dialect, final CodeGenerationParameter param) {
    final String result = param.hasAny(Label.ALIAS) ? param.retrieveRelatedValue(Label.ALIAS) : param.value;
    if(FieldDetail.isValueObjectCollection(param) || !param.hasAny(Label.FIELD_TYPE))
      return result;
    return dialect.isJava() ? result : toPascalCase(result);
  }

  private static String resolveAddMemberFrom(Dialect dialect) {
    return dialect.isJava() ? "add" : "Add";
  }

  private static String resolveAddAllMemberFrom(Dialect dialect) {
    return dialect.isJava() ? "addAll" : "AddRange";
  }

  private static String resolveRemoveMemberFrom(Dialect dialect) {
    return dialect.isJava() ? "remove" : "Remove";
  }

  private static String resolveRemoveAllMemberFrom(Dialect dialect) {
    return dialect.isJava() ? "removeAll" : "RemoveAll";
  }

  private final String symbol;
  private final BiFunction<Dialect, CodeGenerationParameter, List<String>> statementsResolver;

  CollectionMutation(final String symbol, final BiFunction<Dialect, CodeGenerationParameter, List<String>> statementsResolver) {
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
    return statementsResolver.apply(Dialect.findDefault(), methodParameter).stream()
        .map(statement -> collectionOwner + "." + statement)
        .collect(Collectors.toList());
  }

  public List<String> resolveStatements(final Dialect dialect, final String collectionOwner,
                                        final CodeGenerationParameter methodParameter) {
    return statementsResolver.apply(dialect, methodParameter).stream()
        .map(statement -> collectionOwner + "." + statement)
        .collect(Collectors.toList());
  }

  public List<String> resolveStatements(final Dialect dialect, final CodeGenerationParameter methodParameter) {
    return new ArrayList<>(statementsResolver.apply(dialect, methodParameter));
  }

  public List<String> resolveStatements(final String collectionOwner, final String elementOwner,
                                        final CodeGenerationParameter methodParameter) {
    return resolveStatements(collectionOwner, methodParameter)
        .stream().map(statement -> statement.replace("(", "(" + elementOwner + "."))
        .collect(Collectors.toList());
  }

  public List<String> resolveStatements(final Dialect dialect, final String collectionOwner, final String elementOwner,
                                        final CodeGenerationParameter methodParameter) {
    return resolveStatements(dialect, collectionOwner, methodParameter)
        .stream().map(statement -> statement.replace("(", "(" + elementOwner + "."))
        .collect(Collectors.toList());
  }

  public boolean shouldReplaceWithMethodParameter() {
    return equals(REPLACEMENT) || equals(NONE);
  }
}
