package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.designer.task.projectgeneration.restapi.data.ContextSettingsData;

public class IndexPageArguments implements TemplateArguments {

    public final ContextSettingsData context;

    public IndexPageArguments(ContextSettingsData context) {
        this.context = context;
    }
}
