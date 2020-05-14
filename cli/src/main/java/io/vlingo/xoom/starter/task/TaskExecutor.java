// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task;

import java.util.List;

public class TaskExecutor {

    private static boolean AUTOMATIC_EXIT = true;
    private static final int MAIN_COMMAND_INDEX = 0;

    public static void execute(final List<String> args) {
        validateArgs(args);
        Task.trigger(args.get(MAIN_COMMAND_INDEX)).run(args);
    }

    private static void validateArgs(final List<String> args){
        if(args.size() == 0) {
            throw new CommandNotFoundException();
        }
    }

    public static void skipAutomaticExit() {
        AUTOMATIC_EXIT = false;
    }

    public static boolean shouldExit() {
        return AUTOMATIC_EXIT;
    }

}
