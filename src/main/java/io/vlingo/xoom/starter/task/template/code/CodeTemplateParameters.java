// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.starter.task.template.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CodeTemplateParameters {

    private final Map<String, Object> parameters = new HashMap<>();

    private CodeTemplateParameters() {
    }

    public static CodeTemplateParameters empty() {
        return new CodeTemplateParameters();
    }

    public static CodeTemplateParameters with(final CodeTemplateParameter parameter, final Object value) {
        return new CodeTemplateParameters().and(parameter, value);
    }

    public CodeTemplateParameters and(final CodeTemplateParameter parameter, final Object value) {
        this.parameters.put(parameter.key, value);
        return this;
    }

    public CodeTemplateParameters andResolve(final CodeTemplateParameter parameter, final Function<CodeTemplateParameters, Object> resolver) {
        this.parameters.put(parameter.key, resolver.apply(this));
        return this;
    }

    public CodeTemplateParameters enrich(final Consumer<CodeTemplateParameters> enricher)  {
        enricher.accept(this);
        return this;
    }

    public CodeTemplateParameters addImport(final String qualifiedClassName) {
        if(this.find(CodeTemplateParameter.IMPORTS) == null) {
            this.and(CodeTemplateParameter.IMPORTS, new ArrayList<ImportParameter>());
        }
        this.<List>find(CodeTemplateParameter.IMPORTS).add(new ImportParameter(qualifiedClassName));
        return this;
    }

    public <T> T find(final CodeTemplateParameter parameter) {
        return (T) this.parameters.get(parameter.key);
    }

    public Map<String, Object> map() {
        return parameters;
    }
}
