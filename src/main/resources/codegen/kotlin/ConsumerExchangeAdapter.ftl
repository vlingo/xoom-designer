package ${packageName}

import io.vlingo.xoom.lattice.exchange.ExchangeAdapter
import io.vlingo.xoom.lattice.exchange.MessageParameters
import io.vlingo.xoom.lattice.exchange.MessageParameters.DeliveryMode
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class ${exchangeAdapterName} : ExchangeAdapter<${localTypeName}, String, Message> {

  val supportedSchemaName: String

  public fun constructor(supportedSchemaName: String) {
    this.supportedSchemaName = supportedSchemaName
  }

  public override fun fromExchange(exchangeMessage: Message): ${localTypeName} {
    return ${exchangeMapperName}().externalToLocal(exchangeMessage.payloadAsText())
  }

  public override fun toExchange(local: ${localTypeName}): Message {
    val messagePayload = ${exchangeMapperName}().localToExternal(local)
    return Message(messagePayload, MessageParameters.bare().deliveryMode(DeliveryMode.Durable))
  }

  public override boolean supports(exchangeMessage: Object) {
    if(!exchangeMessage.javaClass.equals(Message::class.java)) {
      return false
    }
    val schemaName = (exchangeMessage as Message).messageParameters.typeName()
    return supportedSchemaName.equalsIgnoreCase(schemaName)
  }

}
