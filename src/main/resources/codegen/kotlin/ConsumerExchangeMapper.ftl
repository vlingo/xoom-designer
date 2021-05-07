package ${packageName}

import io.vlingo.xoom.lattice.exchange.ExchangeMapper
import io.vlingo.xoom.common.serialization.JsonSerialization

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ${exchangeMapperName} : ExchangeMapper<${localTypeName}, String> {

  public override fun localToExternal(local: ${localTypeName}): String {
    return JsonSerialization.serialized(local)
  }

  public override fun externalToLocal(external: String): ${localTypeName} {
    return JsonSerialization.deserialized(external, ${localTypeName}::class.java)
  }
}
