// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen;

public class InvalidResourcesPathException extends ProjectGenerationException {
  private static final long serialVersionUID = -2276075547640096725L;

  public InvalidResourcesPathException() {
      super("Please check if the designer path has been set in the environment variables");
  }

}
