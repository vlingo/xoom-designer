// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.designermodel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.restapi.data.DesignerModel;

public class DesignerModelFormatter {

  public static String format(final String designerModel) {
    return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(designerModel));
  }

  public static String format(final DesignerModel designerModel) {
    return format(JsonSerialization.serialized(designerModel));
  }

}

