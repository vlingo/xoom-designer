package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ContextSettingsData;

public class HeaderArguments implements TemplateArguments {

    public final ContextSettingsData context;

    public HeaderArguments(ContextSettingsData context) {
        this.context = context;
    }
}
