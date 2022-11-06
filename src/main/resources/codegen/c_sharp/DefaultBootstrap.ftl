using Com.UserManagement.Account.Infrastructure.Resource;
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Http;
using Vlingo.Xoom.Http.Resource;
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

    var accountResource = new AccountResource(World);
    var frontend = new SinglePageApplicationResource("/frontend", "/app")
    var resources = Resources.Are(accountResource.Routes, frontend.Routes);

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