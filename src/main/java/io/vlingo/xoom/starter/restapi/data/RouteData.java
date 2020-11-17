// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import java.util.List;

public class RouteData {

    public final String path;
    public final String httpMethod;
    public final String aggregateMethod;
    public final Boolean requireEntityLoad;

    public RouteData(final String path,
                     final String httpMethod,
                     final String aggregateMethod,
                     final Boolean requireEntityLoad) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.aggregateMethod = aggregateMethod;
        this.requireEntityLoad = requireEntityLoad;
    }

    public List<String> validate(List<String> errorStrings) {
        if(path==null) errorStrings.add("path is null");
        if(httpMethod==null) errorStrings.add("httpMethod is null");
        if(aggregateMethod==null) errorStrings.add("aggregateMethod is null");
        if(requireEntityLoad==null) errorStrings.add("requireEntityLoad is null");
        return errorStrings;
    }
}
