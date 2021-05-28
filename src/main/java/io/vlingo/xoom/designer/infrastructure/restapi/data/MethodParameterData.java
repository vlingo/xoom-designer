// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

public class MethodParameterData {

  public final String stateField;
  public final String parameterName;
  public final String multiplicity;

  public MethodParameterData(final String stateField) {
    this(stateField, "", "");
  }

  public MethodParameterData(final String stateField,
                             final String parameterName,
                             final String multiplicity) {
    this.stateField = stateField;
    this.parameterName = parameterName;
    this.multiplicity = multiplicity;
  }

}
