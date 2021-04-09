// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task;

public class UnknownCommandException extends TaskExecutionException {

    public UnknownCommandException(final String command) {
        super("The informed command is not supported " + command);
    }

    public UnknownCommandException(final Object args) {
        super("The informed command does not support arguments type " + args.getClass());
    }

}
