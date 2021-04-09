// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer;

import java.util.List;

public class ArgumentRetriever {

    public static String retrieve(final String argumentName, final List<String> args) {
        final int index = args.indexOf(argumentName);
        if(index > 0 && index + 1 < args.size()) {
            final String value = args.get(index+1);
            if(value != null) {
                return value;
            }
        }
        throw new ArgumentNotFoundException(argumentName);
    }

}
