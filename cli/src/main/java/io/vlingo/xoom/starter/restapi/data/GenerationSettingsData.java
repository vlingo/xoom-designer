package io.vlingo.xoom.starter.restapi.data;

import io.vlingo.xoom.starter.task.template.Agent;

import java.util.ArrayList;
import java.util.List;

import static io.vlingo.xoom.starter.task.Property.TARGET_FOLDER;
import static io.vlingo.xoom.starter.task.Task.TEMPLATE_GENERATION;

public class GenerationSettingsData {

    public final ContextSettingsData context;
    public final ModelSettingsData model;
    public final DeploymentSettingsData deployment;
    public final String projectDirectory;

    public GenerationSettingsData(final ContextSettingsData context,
                                  final ModelSettingsData model,
                                  final DeploymentSettingsData deployment,
                                  final String projectDirectory) {
        this.context = context;
        this.model = model;
        this.deployment = deployment;
        this.projectDirectory = projectDirectory;
    }

    public List<String> toArguments() {
        final List<String> args = new ArrayList<>();
        args.add(TEMPLATE_GENERATION.command());
        args.addAll(context.toArguments());
        args.addAll(model.toArguments());
        args.addAll(deployment.toArguments());
        args.add(TARGET_FOLDER.literal());
        args.add(projectDirectory);
        args.add(Agent.argumentKey());
        args.add(Agent.WEB.name());
        return args;
    }
}
