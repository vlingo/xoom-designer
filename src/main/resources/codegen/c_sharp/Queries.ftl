<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

public interface ${queriesName}
{
  Completes<${dataName}> ${queryByIdMethodName}(string id);
  Completes<Collection<${dataName}>> ${queryAllMethodName}();
}