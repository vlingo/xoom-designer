package ${packageName};

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.xoom.turbo.Boot;
import io.vlingo.xoom.http.resource.Configuration.Sizing;
import io.vlingo.xoom.http.resource.Configuration.Timing;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.http.resource.SinglePageApplicationResource;
import io.vlingo.xoom.lattice.grid.Grid;
<#if hasExchange>
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
</#if>

@SuppressWarnings("all")
public class Bootstrap {

  private final Grid grid;
  private final Server server;
  private static Bootstrap instance;

  public Bootstrap(final String nodeName) throws Exception {
    grid = Boot.start("${appName}", nodeName);

<#if hasExchange>
    final ExchangeInitializer exchangeInitializer = new ${exchangeBootstrapName}();
    exchangeInitializer.init(grid);

</#if>
<#list registries as registry>
    final ${registry.className} ${registry.objectName} = new ${registry.className}(grid.world());
</#list>
<#list providers as provider>
    ${provider.className}.using(${provider.arguments});
</#list>

<#list restResources as restResource>
    final ${restResource.className} ${restResource.objectName} = new ${restResource.className}(grid);
</#list>

    Resources allResources = Resources.are(
    <#if restResources?size == 0>
        //Include Rest Resources routes here
    </#if>
    <#list restResources as restResource>
        ${restResource.objectName}.routes(),
    </#list>
        new SinglePageApplicationResource("/frontend", "/app").routes()
    );

    server = Server.startWith(grid.world().stage(), allResources, Boot.serverPort(), Sizing.define(), Timing.define());

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

  public void stopServer() {
    if (instance == null) {
      throw new IllegalStateException("${appName} server not running");
    }
    instance.server.stop();
  }

  public Grid grid() {
    return grid;
  }

  public static void main(final String[] args) throws Exception {
    System.out.println("=========================");
    System.out.println("service: ${appName}.");
    System.out.println("=========================");

    instance = new Bootstrap(parseNodeName(args));
  }

  private static String parseNodeName(final String[] args) {
    if (args.length == 0) {
        return null;
    } else if (args.length > 1) {
        System.out.println("Too many arguments; provide node name only.");
        System.exit(1);
    }
    return args[0];
  }
}
