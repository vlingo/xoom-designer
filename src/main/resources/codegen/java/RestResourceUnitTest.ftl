package ${packageName};

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
import java.time.LocalDate;

public class ${resourceUnitTestName} extends AbstractRestTest {

  @Test
  public void testEmptyResponse() {
    given()
      .when()
      .get("${uriRoot}")
      .then()
      .statusCode(200)
      .body(is(equalTo("[]")));
  }

  private ${dataObjectName} saveExampleData(${dataObjectName} data) {
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

  @Test
  public void ${testCase.methodName}() {
    ${testCase.dataDeclaration}
  <#if testCase.isGetRootMethod()>
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
