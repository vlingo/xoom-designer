<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Xunit;

namespace ${packageName};

public class AbstractRestTest : IDisposable
{

  protected int Port;
  private readonly XoomInitializer _xoom;

  public AbstractRestTest()
  {
    ResolvePort();
    ComponentRegistry.Clear();
    XoomInitializer.Main(new []{"-Dport=" + Port});
    _xoom = XoomInitializer.Instance;
    var startUpSuccess = _xoom.Server.StartUp.Await(100);
    Console.WriteLine("==== Test Server running on " + Port);
    Assert.True(startUpSuccess);
  }

  public void Dispose()
  {
    Console.WriteLine("==== Test Server shutting down ");
    _xoom.terminateWorld();
    WaitServerClose();
  }

  private void WaitServerClose()
  {
    while(_xoom != null && _xoom.Server != null && !_xoom.Server.IsStopped)
    {
      Thread.Sleep(100);
    }
  }

  private void ResolvePort()
  {
    Port = (int) (new Random().NextInt64() * 55535) + 10000;
  }
}