// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.designermodel;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;

public class DesignerModelFormatter {

  public static String format(final String designerModel) {
    return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(designerModel));
  }

  public static String format(final String designerModel, final ExclusionStrategy strategy) {
    return new GsonBuilder().addSerializationExclusionStrategy(strategy)
        .setPrettyPrinting().create().toJson(JsonSerialization.deserialized(designerModel, DesignerModel.class));
  }

  public static String format(final DesignerModel designerModel) {
    return format(JsonSerialization.serialized(designerModel));
  }
  public static String format(final Dialect dialect, final DesignerModel designerModel) {
    return format(dialect, JsonSerialization.serialized(designerModel));
  }

  public static String format(final Dialect dialect, final String designerModel) {
    ExclusionStrategy strategy = new ExclusionStrategy() {
      @Override
      public boolean shouldSkipField(FieldAttributes field) {
        if (!dialect.isJava() && field.getName().equals("groupId")) {
          return true;
        }
        if (!dialect.isJava() && field.getName().equals("artifactId")) {
          return true;
        }
        if (!dialect.isJava() && field.getName().equals("artifactVersion")) {
          return true;
        }
        if (!dialect.isJava() && field.getName().equals("packageName")) {
          return true;
        }
        if (dialect.isJava() && field.getName().equals("solutionName")) {
          return true;
        }
        if (dialect.isJava() && field.getName().equals("projectName")) {
          return true;
        }
        if (dialect.isJava() && field.getName().equals("projectVersion")) {
          return true;
        }
        return dialect.isJava() && field.getName().equals("namespace");
      }

      @Override
      public boolean shouldSkipClass(Class<?> clazz) {
        return false;
      }
    };
    return format(designerModel, strategy);
  }
}

