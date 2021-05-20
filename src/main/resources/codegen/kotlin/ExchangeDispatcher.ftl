package ${packageName}

import io.vlingo.xoom.common.serialization.JsonSerialization
import io.vlingo.xoom.lattice.exchange.Exchange
import io.vlingo.xoom.symbio.Entry
import io.vlingo.xoom.symbio.State
import io.vlingo.xoom.symbio.store.Result
import io.vlingo.xoom.symbio.store.dispatch.ConfirmDispatchedResultInterest
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.stream.Stream
import java.util.Collections
import java.util.stream.Collectors

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#dispatcher-and-projectiondispatcher">
 *   Dispatcher and ProjectionDispatcher
 * </a>
 */
public class ExchangeDispatcher : Dispatcher<Dispatchable<Entry<String>, State<String>>>, ConfirmDispatchedResultInterest {
  companion object {
    val logger = LoggerFactory.getLogger(ExchangeDispatcher::class.java)
  }


  var control:  DispatcherControl
  val producerExchanges: List<Exchange>
  val eventsByExchangeName = HashMap<String, Set<String>>()

  public constructor(vararg producerExchanges: Exchange) {
    <#list producerExchanges as exchange>
    this.eventsByExchangeName.put("${exchange.name}", HashSet<>())
    <#list exchange.events as event>
    this.eventsByExchangeName.get("${exchange.name}").add(${event}::class.java.getCanonicalName())
    </#list>
    </#list>
    this.producerExchanges = Arrays.asList(producerExchanges)
  }

  public override fun dispatch(dispatchable: Dispatchable<Entry<String>, State<String>>) {
    logger.debug("Going to dispatch id {}", dispatchable.id())

    for (entry in dispatchable.entries()) {
      this.send(JsonSerialization.deserialized(entry.entryData(), entry.typed()))
    }

    this.control.confirmDispatched(dispatchable.id(), this)
  }

  public override fun confirmDispatchedResultedIn(result: Result, dispatchId: String) {
      logger.debug("Dispatch id {} resulted in {}", dispatchId, result)
  }

  public override fun controlWith(control: DispatcherControl) {
    this.control = control
  }

  fun send(event: Object) {
    this.findInterestedIn(event).forEach{exchange -> exchange.send(event)}
  }

  fun findInterestedIn(event: Object): Stream<Exchange> {
    val exchangeNames: Set<String> =
          eventsByExchangeName.entrySet().stream().filter{exchange ->
             val events: Set<String> = exchange.getValue()
             events.contains(event.javaClass.getCanonicalName())
         }.map(Map.Entry::getKey).collect(Collectors.toSet())

    return this.producerExchanges.stream().filter{exchange -> exchangeNames.contains(exchange.name())}
  }

}
