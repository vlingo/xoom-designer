// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.code.java;

import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;

import java.util.*;
import java.util.stream.Collectors;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.*;

public class Aggregate {

  public final String aggregateName;
  public final String apiRootPath;
  public final AggregateMethod factoryMethod;
  public final List<Route> routes = new ArrayList<>();
  public final List<Field> stateFields = new ArrayList<>();
  public final List<AggregateMethod> methods = new ArrayList<>();
  public final List<Field> factoryMethodStateFields = new ArrayList<>();
  public final Map<String, String> indexedStateFields = new HashMap<>();

  public Aggregate(final CodeGenerationParameter aggregate) {
    this.aggregateName = aggregate.value;
    this.apiRootPath = aggregate.retrieveRelatedValue(URI_ROOT);
    this.methods.addAll(resolveMethods(aggregate));
    this.routes.addAll(resolveRoutes(aggregate));
    this.stateFields.addAll(resolveStateFields(aggregate));
    this.factoryMethod = resolveFactoryMethod();
    this.factoryMethodStateFields.addAll(resolveFactoryMethodStateFields());
    this.indexedStateFields.putAll(indexStateFields());
  }

  public Route routeTo(final AggregateMethod method) {
    return routes.stream().filter(route -> route.methodName.equals(method.name)).findFirst().orElse(new Route());
  }

  private List<AggregateMethod> resolveMethods(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(AGGREGATE_METHOD).map(AggregateMethod::new).collect(Collectors.toList());
  }

  private List<Field> resolveStateFields(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(STATE_FIELD).map(Field::new).collect(Collectors.toList());
  }

  private AggregateMethod resolveFactoryMethod() {
    return methods.stream().filter(method -> method.useFactory).findFirst().get();
  }

  private List<Route> resolveRoutes(final CodeGenerationParameter aggregate) {
    return aggregate.retrieveAllRelated(ROUTE_SIGNATURE).map(Route::new).collect(Collectors.toList());
  }

  private List<Field> resolveFactoryMethodStateFields() {
    return factoryMethod.parameters.stream()
            .map(param -> stateFields.stream().filter(field -> param.equals(field.name)).findFirst().get())
            .collect(Collectors.toList());
  }

  private Map<String, String> indexStateFields() {
    return this.stateFields.stream()
            .collect(Collectors.toMap(
                    stateField -> stateField.name,
                    stateField -> stateField.type,
                    (a, b) -> a, LinkedHashMap::new));
  }

}
