// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

import java.util.ArrayList;
import java.util.List;

public class ProducerExchangeData {

    public final String exchangeName;
    public final String schemaGroup;
    public final List<String> outgoingEvents = new ArrayList<>();

    public ProducerExchangeData(final String exchangeName,
                                final String schemaGroup) {
        this.exchangeName = exchangeName;
        this.schemaGroup = schemaGroup;
    }

}
