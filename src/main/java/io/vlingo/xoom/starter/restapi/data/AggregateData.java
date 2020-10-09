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
    public final List<StateFieldData> statesFields = new ArrayList<>();
    public final List<AggregateMethodData> methods = new ArrayList<>();
    public final List<DomainEventData> events = new ArrayList<>();

    public AggregateData(final String aggregateName,
                         final APIData api,
                         final List<DomainEventData> domainEvents,
                         final List<StateFieldData> statesFields,
                         final List<AggregateMethodData> methods) {
        this.api = api;
        this.methods.addAll(methods);
        this.events.addAll(domainEvents);
        this.statesFields.addAll(statesFields);
        this.aggregateName = aggregateName;
    }

}