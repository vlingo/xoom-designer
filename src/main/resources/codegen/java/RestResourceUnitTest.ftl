package ${packageName};

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>

public class ${resourceUnitTestName} {

  protected int port;
  private XoomInitializer xoom;

  @BeforeAll
  public static void init() {
    RestAssured.defaultParser = Parser.JSON;
  }

  @BeforeEach
  public void setUp() throws Exception {
    resolvePort();
    ComponentRegistry.clear();
    ComponentRegistry.register(ExchangeInitializer.class, new MockExchangeInitializer());
    XoomInitializer.main(new String[]{"-Dport=" + port});
    xoom = XoomInitializer.instance();
    Boolean startUpSuccess = xoom.server().startUp().await(100);
    System.out.println("==== Test Server running on " + port);
    assertThat(startUpSuccess, is(equalTo(true)));
  }

  @AfterEach
  public void cleanUp() throws InterruptedException {
    System.out.println("==== Test Server shutting down ");
    xoom.terminateWorld();
    waitServerClose();
  }

  <#list testCases as testCase>
  <#list testCase.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  @Test
  public void ${testCase.methodName}() {
    <#list testCase.preliminaryStatements as statement>
    ${statement}
    </#list>
    <#list testCase.statements as statement>

      <#list statement.resultAssignment as resultAssignment>
    ${resultAssignment}
      </#list>

      <#list statement.assertions as assertion>
    ${assertion}
      </#list>
    </#list>
  }

  </#list>


  public RequestSpecification given() {
    return io.restassured.RestAssured.given()
    .filter(new RequestLoggingFilter())
    .filter(new ResponseLoggingFilter())
    .port(port)
    .accept(ContentType.JSON)
    .contentType(ContentType.JSON);
  }

  private void waitServerClose() throws InterruptedException {
    while(xoom != null && xoom.server() != null && !xoom.server().isStopped()) {
      Thread.sleep(100);
    }
  }

  private void resolvePort() {
    port = (int) (Math.random() * 55535) + 10000;
  }
}