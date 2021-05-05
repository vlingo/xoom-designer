
exchange.names=${inlineExchangeNames}

<#list exchangeNames as exchangeName>
exchange.${exchangeName}.hostname=
exchange.${exchangeName}.username=
exchange.${exchangeName}.password=
exchange.${exchangeName}.port=
exchange.${exchangeName}.virtual.host=

</#list>
