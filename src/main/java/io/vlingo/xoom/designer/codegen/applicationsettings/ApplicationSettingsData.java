package io.vlingo.xoom.designer.codegen.applicationsettings;

import io.vlingo.xoom.codegen.template.TemplateData;

import java.util.ArrayList;
import java.util.List;

public abstract class ApplicationSettingsData {

  abstract List<TemplateData> projectSettings();

  abstract TemplateData actorSettings();

  protected List<TemplateData> generate() {
    final List<TemplateData> result = new ArrayList<>(projectSettings());
    result.add(actorSettings());
    return result;
  }
}
