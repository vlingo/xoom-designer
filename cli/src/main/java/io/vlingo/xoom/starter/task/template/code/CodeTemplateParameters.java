// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

import java.util.HashMap;
import java.util.Map;

public class CodeTemplateParameters {

    private final Map<String, Object> parameters = new HashMap<>();

    private CodeTemplateParameters() {
    }

    public static CodeTemplateParameters with(final CodeTemplateParameter parameter, final Object value) {
        return new CodeTemplateParameters().and(parameter, value);
    }

    public CodeTemplateParameters and(final CodeTemplateParameter parameter, final Object value) {
        this.parameters.put(parameter.token, value);
        return this;
    }

    public <T> T from(final CodeTemplateParameter parameter) {
        return (T) this.parameters.get(parameter.token);
    }

    public Map<String, Object> map() {
        return parameters;
    }
}
