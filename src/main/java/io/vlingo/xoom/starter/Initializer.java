// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter;

import io.vlingo.xoom.starter.task.TaskExecutionException;
import io.vlingo.xoom.starter.task.TaskExecutor;

import java.util.Arrays;

public class Initializer {

    public static void main(final String[] args) {
        try {
            TaskExecutor.execute(Arrays.asList(args));
        } catch (final TaskExecutionException exception) {
            System.out.println(exception.getMessage());
        } catch (final Exception exception) {
            exception.printStackTrace();
        } finally {
            if(TaskExecutor.shouldExit()) {
                System.exit(0);
            }
        }
    }

}
