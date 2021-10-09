// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.java.schemata;

import io.vlingo.xoom.terminal.CommandExecutionException;

public class SchemaPushException extends CommandExecutionException {
  private static final long serialVersionUID = 6076113309743794985L;

  public SchemaPushException(final CommandExecutionException exception) {
    super(exception.getCause());
  }

}
