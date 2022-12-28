using Vlingo.Xoom.Common.Serialization;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ${exchangeMapperName} : IExchangeMapper<IdentifiedDomainEvent, Message>
{

  public Message LocalToExternal(IdentifiedDomainEvent event)
  {
    var messagePayload = JsonSerialization.Serialized(event);
    return new Message(messagePayload, MessageParameters.Bare().DeliveryMode(MessageParameters.DeliveryMode.Durable));
  }

  public IdentifiedDomainEvent ExternalToLocal(Message message)
  {
    try
    {
      var eventFullyQualifiedName = message.messageParameters.typeName();

      var eventClass = eventFullyQualifiedName.GetType();

      return JsonSerialization.Deserialized<eventClass>(message.PayloadAsText());
    } catch (TypeNotFoundException e)
    {
      throw new IllegalArgumentException("Unable to handle message containing "
              + message.MessageParameters.TypeName(), e);
    }
  }
}
