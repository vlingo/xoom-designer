@file:JvmName("Bootstrap")

package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.actors.Stage
import io.vlingo.xoom.actors.World
import io.vlingo.xoom.common.identity.IdentityGeneratorType
import io.vlingo.xoom.http.resource.Configuration.Sizing
import io.vlingo.xoom.http.resource.Configuration.Timing
import io.vlingo.xoom.http.resource.Resources
import io.vlingo.xoom.http.resource.Server
import io.vlingo.xoom.lattice.grid.GridAddressFactory

public class Bootstrap {
  companion object {
    var instance: Bootstrap
    var DefaultPort: Int = 18080
  }

  val server: Server
  val world: World

  public constructor(port: Int) {
    world = World.startWithDefaults("${appName}")

    val stage = world.stageNamed("${appName}", Stage::class.java, GridAddressFactory(IdentityGeneratorType.RANDOM))

<#list registries as registry>
    val ${registry.objectName} = ${registry.className}(world)
</#list>
<#list providers as provider>
    ${provider.className}.using(${provider.arguments})
</#list>

<#list restResources as restResource>
    val ${restResource.objectName} = ${restResource.className}(stage)
</#list>

    var allResources = Resources.are(
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
    )

    server = Server.startWith(stage, allResources, port, Sizing.define(), Timing.define())

    Runtime.getRuntime().addShutdownHook(Thread {
        if (instance != null) {
            instance.server.stop()
            println("\n")
            println("=========================")
            println("Stopping \${appName}.")
            println("=========================")
        }
    })
  }

  public void stopServer() {
    if (instance == null) {
      throw new IllegalStateException("${appName} server not running");
    }
    instance.server.stop();

    instance = null;
  }
}

fun main(args: Array<String>) {
  println("=========================")
  println("service: ${appName}.")
  println("=========================")

  var port: Int

  try {
    port = Integer.parseInt(args[0])
  } catch (Exception e) {
    port = DefaultPort
    println("${appName}: Command line does not provide a valid port; defaulting to: " + port)
  }

  Bootstrap.instance = Bootstrap(port)
}
