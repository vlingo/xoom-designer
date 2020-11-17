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

    public List<String> validate(List<String> errorStrings) {
        if(api==null) {
            errorStrings.add("api is null");
        } else {
            api.validate(errorStrings);
        }
        if(aggregateName==null) errorStrings.add("aggregateName is null");
        if(stateFields==null) {
            errorStrings.add("stateFields is null");
        } else {
            stateFields.forEach(
                stateField -> stateField.validate(errorStrings)
            );
        }
        if(methods==null) {
            errorStrings.add("methods is null");
        } else {
            methods.forEach(
                method -> method.validate(errorStrings)
            );
        }
        if(events==null) {
            errorStrings.add("events is null");
        } else {
            events.forEach(
                event -> event.validate(errorStrings)
            );
        }
        return errorStrings;
    }
}