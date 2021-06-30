// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.designermodel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.restapi.data.GenerationSettingsData;

public class DesignerModelFormatter {

  public static String format(final String designerModel) {
    return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(designerModel));
  }

  public static String format(final GenerationSettingsData generationSettingsData) {
    return format(JsonSerialization.serialized(generationSettingsData));
  }

}

