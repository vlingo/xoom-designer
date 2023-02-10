
<#if imports?has_content>
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
</#if>

namespace ${packageName};

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#dispatcher-and-projectiondispatcher">
 *   Dispatcher and ProjectionDispatcher
 * </a>
 */
public class ExchangeDispatcher : IDispatcher<Dispatchable<Entry<string>, IState<string>>>, IConfirmDispatchedResultInterest
{
  private DispatcherControl _control;
  private Exchange _producer;
  private List<string> _outgoingEvents = new ArrayList<>();

  public ExchangeDispatcher(Exchange producer)
  {
    this.producer = producer;
    <#list producerExchanges as exchange>
    <#list exchange.events as event>
    _outgoingEvents.Add(nameof(${event}));
    </#list>
    </#list>
  }

  public void Dispatch(Dispatchable<IEntry<string>, State<string>> dispatchable)
  {
    foreach (var entry in dispatchable.Entries())
    {
      Send(JsonSerialization.Deserialized(entry.EntryData(), entry.Typed()));
    }

    _control.ConfirmDispatched(dispatchable.Id, this);
  }

  public void ConfirmDispatchedResultedIn(Result result, string dispatchId)
  {
  }

  public void ControlWith(DispatcherControl control)
  {
    this.control = control;
  }

  private void Send(Object event)
  {
    if(ShouldPublish(event))
    {
      _producer.Send(event);
    }
  }

  private bool ShouldPublish(Object event)
  {
    return _outgoingEvents.Contains(nameof(event));
  }

}
