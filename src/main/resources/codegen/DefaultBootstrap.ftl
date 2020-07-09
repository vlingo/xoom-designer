package ${packageName};

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.GridAddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.http.resource.Configuration.Sizing;
import io.vlingo.http.resource.Configuration.Timing;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;

public class Bootstrap {
  private static Bootstrap instance;
  private static int DefaultPort = 18080;

  private final Server server;
  private final World world;

  public Bootstrap(final int port) throws Exception {
    world = World.startWithDefaults("${appName}");

    final Stage stage =
            world.stageNamed("${appName}", Stage.class, new GridAddressFactory(IdentityGeneratorType.RANDOM));

<#list registries as registry>
    final ${registry.className} ${registry.objectName} = new ${registry.className}(world);
</#list>
<#list providers as provider>
    ${provider.initialization}.using(${provider.arguments});
</#list>

<#list restResources as restResource>
    final ${restResource.className} ${restResource.objectName} = new ${restResource.className}(stage);
</#list>

    Resources allResources = Resources.are(
    <#if restResources?size == 0>
        //Include Rest Resources routes here
    </#if>
    <#list restResources as restResource>
        <#if restResource.last>
        ${restResource.objectName}.routes()
        <#else>
        ${restResource.objectName}.routes(),
        </#if>
    </#list>
    );

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define());

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
