
      <plugin>
        <groupId>io.vlingo.xoom</groupId>
        <artifactId>xoom-build-plugins</artifactId>
        <version>${r"${vlingo.xoom.version}"}</version>
        <executions>
          <#if hasProducerExchange>

          <execution>
            <id>push</id>
            <!-- Run to push the schema to schemata: `mvn io.vlingo.xoom:xoom-build-plugins:push-schema@push` -->
            <!-- Uncomment the following line to automatically push the schema during `mvn deploy`: -->
            <!-- <phase>deploy</phase> -->
            <goals>
              <goal>push-schema</goal>
            </goals>
            <configuration>
              <srcDirectory>${r"${basedir}"}/src/main/vlingo/schemata</srcDirectory>
              <schemataService>
                <url>http://${schemataSettings.host}:${schemataSettings.port?c}</url>
                <clientOrganization>${producerOrganization}</clientOrganization>
                <clientUnit>${producerUnit}</clientUnit>
                <hierarchicalCascade>true</hierarchicalCascade>
              </schemataService>
              <schemata>
                <#list producerSchemas as schema>
                <schema>
                  <ref>${schema.reference}</ref>
                  <src>${schema.file}</src>
                </schema>
                </#list>
              </schemata>
            </configuration>
          </execution>
          </#if>
          <#if hasConsumerExchange>

          <execution>
            <id>pull</id>
            <goals>
              <goal>pull-schema</goal>
            </goals>
            <configuration>
              <schemataService>
                <url>http://localhost:9019</url>
                <clientOrganization>Inform the client organization</clientOrganization>
                <clientUnit>Inform the client unit</clientUnit>
              </schemataService>
              <schemata>
                <#list consumerSchemas as schema>
                <schema>
                  <ref>${schema.reference}</ref>
                </schema>
                </#list>
              </schemata>
            </configuration>
          </execution>

          </#if>
        </executions>
      </plugin>
