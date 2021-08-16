<#if deploymentSettings.useDocker>
docker.image=${deploymentSettings.dockerImage}
</#if>
<#if deploymentSettings.useKubernetes>
docker.repository=${deploymentSettings.kubernetesImage}
</#if>
xoom.http.server.port=${turboSettings.httpServerPort?c}

<#if turboSettings.useLocalExchange>
xoom.lattice.exchange.local.port=${turboSettings.localExchangePort?c}
</#if>