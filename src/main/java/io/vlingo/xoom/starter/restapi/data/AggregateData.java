// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateData {

    public final String name;
    public final List<DomainEventData> events = new ArrayList<>();

    public AggregateData(final String name, final List<DomainEventData> events) {
        this.name = name;
        this.events.addAll(events);
    }

    public String flatEvents() {
        return events.stream().map(event -> event.name).collect(Collectors.joining(";"));
    }
}
