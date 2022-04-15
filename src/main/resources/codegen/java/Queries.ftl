package ${packageName};

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

@SuppressWarnings("all")
public interface ${queriesName} {
  <#if compositeId?has_content>
  Completes<${dataName}> ${queryByIdMethodName}(${compositeId}String id);
  Completes<Collection<${dataName}>> ${queryAllMethodName}(${compositeId?substring(0, compositeId?length - 2)});
  <#else>
  Completes<${dataName}> ${queryByIdMethodName}(String id);
  Completes<Collection<${dataName}>> ${queryAllMethodName}();
  </#if>
}