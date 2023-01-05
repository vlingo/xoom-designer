// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.report;

public class ReportFormattingException extends RuntimeException {
  private static final long serialVersionUID = -8697632337724723343L;

  public ReportFormattingException(final Exception exception) {
    super(exception);
  }

}
