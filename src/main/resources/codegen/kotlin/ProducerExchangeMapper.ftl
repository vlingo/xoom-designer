package ${packageName}

import io.vlingo.xoom.common.serialization.JsonSerialization
import io.vlingo.xoom.lattice.exchange.ExchangeMapper
import io.vlingo.xoom.lattice.exchange.MessageParameters
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ${exchangeMapperName} : ExchangeMapper<IdentifiedDomainEvent, Message> {

  public override fun localToExternal(event: IdentifiedDomainEvent): Message {
    val messagePayload = JsonSerialization.serialized(event)
    return Message(messagePayload, MessageParameters.bare().deliveryMode(MessageParameters.DeliveryMode.Durable))
  }

  public override fun externalToLocal(message:  Message): IdentifiedDomainEvent {
    try {
      val eventFullyQualifiedName = message.messageParameters.typeName()

      val eventClass = (Class<? extends IdentifiedDomainEvent>) Class.forName(eventFullyQualifiedName)

      return JsonSerialization.deserialized(message.payloadAsText(), eventClass)
    } catch (e: ClassNotFoundException) {
      throw IllegalArgumentException("Unable to handle message containing "
              + message.messageParameters.typeName(), e)
    }
  }
}
