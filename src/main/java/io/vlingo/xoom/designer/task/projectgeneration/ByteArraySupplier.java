// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import java.io.ByteArrayOutputStream;

public class ByteArraySupplier {

  private ByteArrayOutputStream stream;

  public static ByteArraySupplier empty() {
    return new ByteArraySupplier();
  }

  public void stream(final ByteArrayOutputStream stream) {
    this.stream = stream;
  }

  public byte[] get() {
    if(stream == null) {
      return new byte[0];
    }
    return stream.toByteArray();
  }
}
