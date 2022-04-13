package io.vlingo.xoom.designer.codegen.csharp;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vlingo.xoom.designer.codegen.java.TemplateParameter.APPLICATION_NAME;

public enum CsharpTemplateStandard implements TemplateStandard {

  SOLUTION_SETTINGS(parameters -> Template.SOLUTION_SETTINGS.filename,
      (name, parameters) -> parameters.find(APPLICATION_NAME) + ".sln"),

  PROJECT_SETTINGS(parameters -> Template.PROJECT_SETTINGS.filename,
      (name, parameters) -> parameters.find(APPLICATION_NAME) + ".csproj"),

  ACTOR_SETTINGS(parameters -> Template.ACTOR_SETTINGS.filename,
      (name, parameters) -> "vlingo-actors.json");

  private final Function<TemplateParameters, String> templateFileRetriever;
  private final BiFunction<String, TemplateParameters, String> nameResolver;

  CsharpTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever) {
    this(templateFileRetriever, (name, parameters) -> name);
  }

  CsharpTemplateStandard(final Function<TemplateParameters, String> templateFileRetriever,
                         final BiFunction<String, TemplateParameters, String> nameResolver) {
    this.templateFileRetriever = templateFileRetriever;
    this.nameResolver = nameResolver;
  }

  public String retrieveTemplateFilename(final TemplateParameters parameters) {
    return templateFileRetriever.apply(parameters);
  }

  public String resolveClassname() {
    return resolveClassname("");
  }

  public String resolveClassname(final String name) {
    return resolveClassname(name, null);
  }

  public String resolveClassname(final TemplateParameters parameters) {
    return resolveClassname(null, parameters);
  }

  public String resolveClassname(final String name, final TemplateParameters parameters) {
    return this.nameResolver.apply(name, parameters);
  }

  public String resolveFilename(final TemplateParameters parameters) {
    return resolveFilename(null, parameters);
  }

  public String resolveFilename(final String name, final TemplateParameters parameters) {
    return this.nameResolver.apply(name, parameters);
  }

}
