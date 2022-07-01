package ${packageName};

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

<#if testCases?filter(testCase -> testCase.selfDescribingEvents()?has_content)?has_content>
// TODO: model uses self-describing event(s) ${testCases[0].selfDescribingEvents()}, 
// which may cause test failure based on assumed default values.
// Run tests to find any such failed tests and correct the incorrectly
// assumed defaults and transitions.
</#if>
public class ${resourceUnitTestName} extends AbstractRestTest {

  @Test
  public void testEmptyResponse() {
  <#list compositeId as declaration>
    ${declaration}
  </#list>
    given()
      .when()
      .get("${uriRoot}")
      .then()
      .statusCode(200)
      .body(is(equalTo("[]")));
  }

  private ${dataObjectName} saveExampleData(${dataObjectName} data) {
  <#list compositeId as declaration>
    ${declaration}
  </#list>
    return given()
      .when()
      .body(data)
      .post("${uriRoot}")
      .then()
      .statusCode(201)
      .extract()
      .body()
      .as(${dataObjectName}.class);
  }
<#list testCases as testCase>

  <#if testCase.isDisabled()>
  // TODO:
  // This test is disabled and in need of custom code modifications. The
  // parameters to command message ${testCase.getMethodName()} of Aggregate type
  // ${aggregateProtocolName} do not map explicitly to state or event.
  // You must provide some test case values in this method.
  @Disabled
  <#elseif testCase.isMethodEmitSelfDescribingEvent()>
  @Disabled
  </#if>
  @Test
  public void ${testCase.methodName}() {
  <#list compositeId as declaration>
    ${declaration}
  </#list>
    ${testCase.dataDeclaration}
  <#if testCase.isRootMethod()>
    firstData = saveExampleData(firstData);
  </#if>
  <#list testCase.preliminaryStatements as statement>
    ${statement}
  </#list>
  <#list testCase.statements as statement>
  <#list statement.assertions as assertion>
    ${assertion}
  </#list>
  </#list>
  }
</#list>
}
