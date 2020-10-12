// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class AggregateData {

    public final APIData api;
    public final String aggregateName;
    public final List<StateFieldData> stateFields = new ArrayList<>();
    public final List<AggregateMethodData> methods = new ArrayList<>();
    public final List<DomainEventData> events = new ArrayList<>();

    public AggregateData(final String aggregateName,
                         final APIData api,
                         final List<DomainEventData> domainEvents,
                         final List<StateFieldData> stateFields,
                         final List<AggregateMethodData> methods) {
        this.api = api;
        this.methods.addAll(methods);
        this.events.addAll(domainEvents);
        this.stateFields.addAll(stateFields);
        this.aggregateName = aggregateName;
    }

}