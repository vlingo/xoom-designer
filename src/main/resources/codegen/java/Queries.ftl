package ${packageName};

import java.util.Collection;
import io.vlingo.xoom.common.Completes;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

@SuppressWarnings("all")
public interface ${queriesName} {
  Completes<${dataName}> ${queryByIdMethodName}(String id);
  Completes<Collection<${dataName}>> ${queryAllMethodName}();
}