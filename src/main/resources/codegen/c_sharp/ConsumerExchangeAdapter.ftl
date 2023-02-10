<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class ${exchangeAdapterName} : IExchangeAdapter<${localTypeName}, string, Message>
{

  private string _supportedSchemaName;

  public ${exchangeAdapterName}(string supportedSchemaName)
  {
    _supportedSchemaName = supportedSchemaName;
  }

  public ${localTypeName} FromExchange(Message exchangeMessage)
  {
    return new ${exchangeMapperName}().ExternalToLocal(exchangeMessage.PayloadAsText());
  }

  public Message ToExchange(${localTypeName} local)
  {
    var messagePayload = new ${exchangeMapperName}().LocalToExternal(local);
    return new Message(messagePayload, MessageParameters.Bare().DeliveryMode(DeliveryMode.Durable));
  }

  public bool supports(Object exchangeMessage)
  {
    if(typeof(exchangeMessage) != typeof(Message))
    {
      return false;
    }
    var schemaName = ((Message) exchangeMessage).MessageParameters.TypeName();
    return _supportedSchemaName.EqualsIgnoreCase(schemaName);
  }

}
