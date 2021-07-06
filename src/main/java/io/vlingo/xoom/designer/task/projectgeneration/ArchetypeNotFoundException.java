// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

public class ArchetypeNotFoundException extends ProjectGenerationException {
  private static final long serialVersionUID = -3872607533773452802L;

  public ArchetypeNotFoundException() {
      super("Unable to find a Template based on properties. Please check if it contains all required properties.");
  }
}
