package io.vlingo.xoom.designer.task.reactjs;

import io.vlingo.xoom.codegen.CodeGenerationContext;

import static io.vlingo.xoom.designer.task.projectgeneration.Label.ARTIFACT_ID;
import static io.vlingo.xoom.designer.task.projectgeneration.Label.GROUP_ID;

public class IndexPageArguments {

    public final String groupId;
    public final String artifactId;

    public IndexPageArguments(final CodeGenerationContext context) {
        this.groupId = context.parameterOf(GROUP_ID);
        this.artifactId = context.parameterOf(ARTIFACT_ID);
    }
}
