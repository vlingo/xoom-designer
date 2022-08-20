using System.Collections.Concurrent;
using Vlingo.Xoom.Actors.TestKit;
using Vlingo.Xoom.Lattice.Model.Projection;

namespace ${packageName};

public class CountingProjectionControl: IProjectionControl
{
  private AccessSafely _access;
  private readonly ConcurrentDictionary<string, int> _confirmations = new ConcurrentDictionary<string, int>();

  public void ConfirmProjected(string projectionId)
  {
    _access.WriteUsing("confirmations", projectionId);
  }

  public Confirmer ConfirmerFor(IProjectable projectable, IProjectionControl control)
  {
    return null;
  }

  public AccessSafely AfterCompleting(int times)
  {
    _access = AccessSafely.AfterCompleting(times);
    _access.WritingWith("confirmations", (string projectionId) => {
      var count = _confirmations.GetValueOrDefault(projectionId);
      _confirmations.TryAdd(projectionId, count + 1);
    });
    _access.ReadingWith("confirmations", () => _confirmations);
    return _access;
  }
}