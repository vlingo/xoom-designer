package ${packageName};

import io.vlingo.xoom.lattice.exchange.ExchangeMapper;
import io.vlingo.xoom.common.serialization.JsonSerialization;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ${exchangeMapperName} implements ExchangeMapper<${localTypeName},String> {

  @Override
  public String localToExternal(final ${localTypeName} local) {
    return JsonSerialization.serialized(local);
  }

  @Override
  public ${localTypeName} externalToLocal(final String external) {
    return JsonSerialization.deserialized(external, ${localTypeName}.class);
  }
}
