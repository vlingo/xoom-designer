// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.codegen.csharp.bootstrap;

import io.vlingo.xoom.codegen.CodeGenerationContext;

public class DefaultBootstrapTemplateData extends BootstrapTemplateData {

  @Override
  protected void enrichParameters(final CodeGenerationContext context) {
  }

  @Override
  protected boolean support(final CodeGenerationContext context) {
    return true;
  }

}
