// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.infrastructure.restapi.data;

import io.vlingo.xoom.http.resource.serialization.JsonSerialization;

public class GenerationPath {
  public final String path;

  public GenerationPath(final String path) {
    this.path = path;
  }

  public String serialized() {
    return JsonSerialization.serialized(this);
  }
}
