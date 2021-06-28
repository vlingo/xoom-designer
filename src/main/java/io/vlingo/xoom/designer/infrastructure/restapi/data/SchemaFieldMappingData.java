// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

public class SchemaFieldMappingData {

  public final String schemaFieldName;
  public final String methodParameterName;

  public SchemaFieldMappingData(String schemaFieldName, String methodParameterName) {
    this.schemaFieldName = schemaFieldName;
    this.methodParameterName = methodParameterName;
  }

}
