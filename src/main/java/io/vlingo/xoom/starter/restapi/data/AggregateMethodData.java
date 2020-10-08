// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.restapi.data;

import java.util.List;

public class AggregateMethodData {

    public final String name;
    public final List<String> parameters;
    public final Boolean factory;
    public final String event;

    public AggregateMethodData(final String name,
                               final List<String> parameters,
                               final Boolean factory,
                               final String event) {
        this.name = name;
        this.event = event;
        this.factory = factory;
        this.parameters = parameters;
    }

}