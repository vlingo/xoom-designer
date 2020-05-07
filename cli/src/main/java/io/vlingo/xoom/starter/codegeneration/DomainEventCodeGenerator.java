// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.codegeneration;

public class DomainEventCodeGenerator extends BaseGenerator {

    private static DomainEventCodeGenerator instance;

    private DomainEventCodeGenerator() {
    }

    public String generate(final String domainEventName, final String packageName) {
        return generate(CodeTemplate.DOMAIN_EVENT.filename, domainEventName, packageName);
    }

    public String generatePlaceholderEvent(final String domainEventName, final String packageName) {
        return generate(CodeTemplate.PLACEHOLDER_DOMAIN_EVENT.filename, domainEventName, packageName);
    }

    private String generate(final String template, final String domainEventName, final String packageName) {
        this.template = template;
        this.input("domainEventName", domainEventName);
        this.input("packageName", packageName);
        return generate();
    }

    public static DomainEventCodeGenerator instance() {
        if(instance == null) {
            instance = new DomainEventCodeGenerator();
        }
        return instance;
    }

}
