// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration.code.java.schemata;

import io.vlingo.xoom.designer.task.projectgeneration.code.java.SchemataSettings;

public class SchemataServiceProfileResolver {

  private static final String PROFILE_NAME = "schemata-service";

  public static String resolveSchemataProfile(final SchemataSettings schemataSettings) {
    if(schemataSettings.serviceName.isPresent()) {
      return "-P" + PROFILE_NAME;
    }
    return "";
  }

}
