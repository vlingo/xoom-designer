package ${packageName}

import java.util.Collection
import io.vlingo.xoom.common.Completes

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

public interface ${queriesName} {
  fun ${queryByIdMethodName}(id: String): Completes<${dataName}>
  fun ${queryAllMethodName}(): Completes<Collection<${dataName}>>
}
