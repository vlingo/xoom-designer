using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using Vlingo.Xoom.Actors.TestKit;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Dispatch;

namespace ${packageName};

public class ${dispatcherName} : IDispatcher
{

  private AccessSafely _access;
  private readonly ConcurrentQueue<string> _states = new ConcurrentQueue<string>();

  public ${dispatcherName}() : base()
  {
    this._access = AfterCompleting(0);
  }

  public AccessSafely afterCompleting( int times)
  {
    _access = AccessSafely.AfterCompleting(times)
      .WritingWith<string>("stored", (string type) -> _states.Enqueue(type))
      .ReadingWith("storedAt", (int index) -> _states.ElementAtOrDefault(index))
      .ReadingWith("storeCount", () -> _states.Count);

    return _access;
  }

  public void ControlWith(IDispatcherControl control)
  {
  }

  public void Dispatch(Dispatchable dispatchable)
  {
    _access.WriteUsing("stored", dispatchable.State.Get());
  }
}
