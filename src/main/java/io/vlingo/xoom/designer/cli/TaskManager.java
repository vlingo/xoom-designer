// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.cli;

import java.util.List;

public interface TaskManager<T> {

    void run(final T args);

    default T manage(final T args) {
        run(args);
        return args;
    }

    default boolean support(final Object args) {
        if(args instanceof List) {
            final List<?> argsList = (List<?>) args;
            if(argsList != null && !argsList.isEmpty()) {
                return argsList.stream().allMatch(arg -> arg.getClass().equals(String.class));
            }
        }
        return false;
    }

}
