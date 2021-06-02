
exchange.names=${inlineExchangeNames}

<#list exchanges as exchange>
exchange.${exchange.name}.hostname=localhost
exchange.${exchange.name}.username=guest
exchange.${exchange.name}.password=guest
exchange.${exchange.name}.port=${exchange.port?c}
exchange.${exchange.name}.virtual.host=/

</#list>
