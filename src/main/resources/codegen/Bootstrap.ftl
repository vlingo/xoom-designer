package ${packageName};

import java.io.IOException;
import java.net.ServerSocket;

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final int port;
  private final Server server;
  private final World world;

  public Bootstrap() throws Exception {
    world = World.startWithDefaults("${appName}");

    world.stageNamed("${appName}", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

<#if projectionDispatcherProvider??>
    final ProjectionDispatcherProvider projectionDispatcherProvider =
            ProjectionDispatcherProvider.using(world.stage());
</#if>

<#if commandModelJournalProvider??>
    final SourcedTypeRegistry registry = new SourcedTypeRegistry(world);
    CommandModelJournalProvider.initialize(world, registry, projectionDispatcherProvider.storeDispatcher);
<#else>
    final StatefulTypeRegistry registry = new StatefulTypeRegistry(world);
    CommandModelStateStoreProvider.initialize(world, registry, projectionDispatcherProvider.storeDispatcher);
</#if>

    QueryModelStateStoreProvider.using(world.stage(), registry, projectionDispatcherProvider.instance().storeDispatcher);

<#if resourcesDeclarations??>
<#list resourcesDeclarations as resourcesDeclaration>
    ${resourcesDeclaration}
</#list>

    Resources allResources = Resources.are(
<#list resourcesRoutes as resourcesRoute>
            ${resourcesRoute}
</#list>
    );
</#if>

    server = Server.startWith(world.stage(), allResources, port, 2);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (instance != null) {
        instance.server.stop();

        System.out.println("\n");
        System.out.println("=========================");
        System.out.println("Stopping ${appName}.");
        System.out.println("=========================");
      }
    }));
  }

  void stopServer() throws Exception {
    if (instance == null) {
      throw new IllegalStateException("Schemata server not running");
    }
    instance.server.stop();
  }

  public static void main(final String[] args) throws Exception {
    System.out.println("=========================");
    System.out.println("service: ${appName}.");
    System.out.println("=========================");

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (Exception e) {
      port = DefaultPort;
      System.out.println("${appName}: Command line does not provide a valid port; defaulting to: " + port);
    }

    instance = new Bootstrap(port);
  }
}
