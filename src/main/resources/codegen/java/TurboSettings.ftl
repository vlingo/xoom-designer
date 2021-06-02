xoom.http.server.port=${turboSettings.httpServerPort?c}
<#if turboSettings.useLocalExchange>
xoom.lattice.exchange.local.port=${turboSettings.localExchangePort?c}
</#if>