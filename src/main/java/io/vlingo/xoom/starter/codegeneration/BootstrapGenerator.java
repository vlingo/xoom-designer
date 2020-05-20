package io.vlingo.xoom.starter.codegeneration;

import java.util.ArrayList;
import java.util.List;

public class BootstrapGenerator {
  private final List<String> resourcesDeclarations;
  private final List<String> resourcesRoutes;

  public BootstrapGenerator(final String appName) {
    this.resourcesDeclarations = new ArrayList<>();
    this.resourcesRoutes = new ArrayList<>();

//    input.put("appName", appName);
//    input.put("resourcesDeclarations", resourcesDeclarations);
//    input.put("resourcesRoutes", resourcesRoutes);
  }

  public void inputOfCommandModelJournalProvider(final boolean commandModelJournalProvider) {
//    input.put("commandModelJournalProvider", commandModelJournalProvider);
  }

  public void inputOfCommandModelStateStoreProvider(final boolean commandModelStateStoreProvider) {
//    input.put("commandModelStateStoreProvider", commandModelStateStoreProvider);
  }

  public void inputOfProjectionDispatcherProvider(final boolean projectionDispatcherProvider) {
//    input.put("projectionDispatcherProvider", projectionDispatcherProvider);
  }

  public void inputOfResource(final String resourceType) {
    final String resourceName = resourceType.substring(0, 1).toLowerCase().concat(resourceType.substring(1));

    resourcesDeclarations.add(resourceDeclaration(resourceType, resourceName));

    prepareResourcesRoutes();
    resourcesRoutes.add(resourceRoute(resourceName));
  }

  private void prepareResourcesRoutes() {
    final int size = resourcesRoutes.size();

    if (size > 0) {
      final int last = size - 1;

      resourcesRoutes.set(last, resourcesRoutes.get(last) + ",");
    }
  }

  private String resourceDeclaration(final String resourceType, final String resourceName) {
    final StringBuilder builder = new StringBuilder()
      .append("final ")
      .append(resourceType)
      .append(" ")
      .append(resourceName)
      .append(" = new ")
      .append(resourceType)
      .append("(world);");

    return builder.toString();
  }

  private String resourceRoute(final String resourceName) {
    final StringBuilder builder = new StringBuilder().append(resourceName).append(".routes()");
    return builder.toString();
  }
}
