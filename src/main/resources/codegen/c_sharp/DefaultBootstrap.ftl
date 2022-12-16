<#list imports as import>
using ${import.qualifiedClassName?substring(0, import.qualifiedClassName?last_index_of("."))};
</#list>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Http;
using Vlingo.Xoom.Http.Resource;
using Vlingo.Xoom.Lattice.Model.Stateful;
using Configuration = Vlingo.Xoom.Http.Resource.Configuration;

namespace ${packageName};

public class Bootstrap
{
  public static Bootstrap Instance { get; private set; }

  public World World { get; }
  public IServer Server { get; }

  public static void Main(string[] args)
  {
    Instance = new Bootstrap();
  }

  private Bootstrap()
  {
    World = World.Start("${appName}");

    <#list registries as registry>
    var ${registry.objectName} = new ${registry.className}(World);
    </#list>
    <#list providers as provider>
    ${provider.className}.Using(${provider.arguments});

    </#list>
    <#list restResources as restResource>
    var ${restResource.objectName} = new ${restResource.className}(World);
    </#list>
    var resources = Resources.Are(
      <#if restResources?size == 0>
      //Include Rest Resources routes here
      </#if>
      <#list restResources as restResource>
      ${restResource.objectName}.Routes
      </#list>
    );

    Server = ServerFactory.StartWith(World.Stage, resources, Filters.None(), Configuration.Define().Port,
      Configuration.SizingConf.DefineWith(4, 10, 100, 10240), Configuration.TimingConf.DefineWith(3, 1, 100),
      "arrayQueueMailbox", "arrayQueueMailbox");

    Console.ReadKey();

    AppDomain.CurrentDomain.ProcessExit += (s, e) =>
    {
      if (Instance != null)
      {
        Instance.Server.Stop();

        Console.WriteLine("\n");
        Console.WriteLine("==============================");
        Console.WriteLine("Stopping ${appName} Server...");
        Console.WriteLine("==============================");
      }
    };
  }
}