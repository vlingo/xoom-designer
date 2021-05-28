// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

public class MethodParameterData {

  public final String name;
  public final String alias;
  public final String collectionMutationSymbol;

  public MethodParameterData(final String name) {
    this(name, "", "");
  }

  public MethodParameterData(final String name,
                             final String alias,
                             final String collectionMutationSymbol) {
    this.name = name;
    this.alias = alias;
    this.collectionMutationSymbol = collectionMutationSymbol;
  }

}
