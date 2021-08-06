
      <profile>
        <id>schemata-service</id>
        <activation>
          <activeByDefault>false</activeByDefault>
        </activation>
        <properties>
          <name>${serviceName}</name>
          <#if servicePort?has_content>
          <port>${servicePort?c}</port>
          </#if>
        </properties>
      </profile>