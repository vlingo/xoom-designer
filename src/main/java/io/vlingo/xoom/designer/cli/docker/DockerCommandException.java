// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.cli.docker;

import io.vlingo.xoom.designer.cli.TaskExecutionException;

public class DockerCommandException extends TaskExecutionException {
  private static final long serialVersionUID = 1566353612015507437L;

  public DockerCommandException(final String message) {
      super(message);
  }

}
