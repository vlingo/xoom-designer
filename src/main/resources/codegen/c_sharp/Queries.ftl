<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using System.Collections.ObjectModel;
using Vlingo.Xoom.Common;

namespace ${packageName};

public interface ${queriesName}
{
  ICompletes<${dataName}> ${queryByIdMethodName}(string id);
  ICompletes<IEnumerable<${dataName}>> ${queryAllMethodName}();
}