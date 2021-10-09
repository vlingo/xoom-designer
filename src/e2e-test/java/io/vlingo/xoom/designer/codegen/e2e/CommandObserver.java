// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.codegen.e2e;

import io.vlingo.xoom.terminal.ObservableCommandExecutionProcess;

public class CommandObserver implements ObservableCommandExecutionProcess.CommandExecutionObserver {

  public ExecutionStatus status;

  public CommandObserver() {
    status = ExecutionStatus.NEW;
  }

  @Override
  public void onSuccess() {
    status = ExecutionStatus.SUCCEEDED;
  }

  @Override
  public void onFailure() {
    status = ExecutionStatus.FAILED;
  }

  public void reset() {
    status = ExecutionStatus.NEW;
  }

  public void stop() {
    status = ExecutionStatus.STOPPED;
  }
}
