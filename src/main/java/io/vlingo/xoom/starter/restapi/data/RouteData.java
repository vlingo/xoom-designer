// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

public class RouteData {

    public final String route;
    public final String httpMethod;
    public final String aggregateMethod;
    public final Boolean requireEntityLoad;

    public RouteData(final String route,
                     final String httpMethod,
                     final String aggregateMethod,
                     final Boolean requireEntityLoad) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.aggregateMethod = aggregateMethod;
        this.requireEntityLoad = requireEntityLoad;
    }

}
