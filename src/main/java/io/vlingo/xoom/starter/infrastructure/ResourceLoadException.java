// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.infrastructure;

import java.nio.file.Path;

public class ResourceLoadException extends RuntimeException {

  public ResourceLoadException(final Path path) {
    super("Unable to load: " + path.toString());
  }

  public ResourceLoadException(final String message) {
    super(message);
  }

}
