<#ftl strip_whitespace=true/>
import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import LoadingOrFailed from "../LoadingOrFailed";
import ${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(aggregate.factoryMethod.name)} from "./${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(aggregate.factoryMethod.name)}";
<#macro printTableHeaderCell name type>
    <#if valueTypes[type]??>
        <#list valueTypes[type] as subType>
            <@printTableHeaderCell "${name} ${subType.name}" subType.type/>
        </#list>
    <#else>
            <th>${fns.capitalizeMultiWord(name)}</th>
    </#if>
</#macro>
<#macro printTableCell name type>
    <#if valueTypes[type]??>
        <#list valueTypes[type] as subType>
            <@printTableCell "${name}?.${subType.name}" subType.type/>
        </#list>
    <#else>
      <#if type=="LocalDate">
          <td>{new Intl.DateTimeFormat('en-GB', {
            month: 'long',
            day: '2-digit',
            year: 'numeric',
            }).format(new Date(item?.${name}))}</td>
      <#else>
          <td>{item?.${name}}</td>
      </#if>
    </#if>
</#macro>
<#macro printJSON fields level=0>
  <@compress single_line=true>
    {<#list fields as field>
        ${field.name}: <#if valueTypes[field.type]??><@printJSON valueTypes[field.type] /><#else>''</#if><#if field?has_next>,</#if>
    </#list>}
  </@compress>
</#macro>

const EMPTY_FORM = <@printJSON aggregate.factoryMethodStateFields />;

const ${fns.capitalize(fns.makePlural(aggregate.aggregateName))} = () => {

  const [loading, setLoading] = useState(false);
  const [items, setItems] = useState([]);
  const [currentModal, setCurrentModal] = useState(null);

  const loadItems = useCallback(() => {
    axios.get('${aggregate.apiRootPath}')
      .then(res => res.data)
      .then(data => {
        console.log('${aggregate.aggregateName} axios success', data);
        setItems(data);
      })
      .catch((e) => {
        console.error('${aggregate.aggregateName} axios failed', e);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const onModalActionComplete = useCallback((data) => {
    loadItems();
    setCurrentModal(null);
  }, [loadItems]);

  const _${aggregate.factoryMethod.name} = useCallback((e) => {
    console.log('showing ${aggregate.factoryMethod.name} modal');
    setCurrentModal(<${aggregate.aggregateName}${fns.capitalize(aggregate.factoryMethod.name)} defaultForm={<@printJSON aggregate.factoryMethodStateFields />} complete={onModalActionComplete}/>);
  }, [onModalActionComplete]);

  useEffect(() => {
    setLoading(true);
    loadItems();
  }, [loadItems]);

  return (
    <>
      <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h2">${fns.makePlural(aggregate.aggregateName)}</h1>
        <div className="btn-toolbar mb-2 mb-md-0">
          <div className="btn-group me-2">
            <button type="button" className="btn btn-sm btn-outline-secondary" onClick={_${aggregate.factoryMethod.name}}>${fns.capitalize(aggregate.factoryMethod.name)}</button>
          </div>
        </div>
      </div>
      <div>
        {
        items ?
        <table className={'table table-striped table-bordered'}>
        <thead>
          <tr>
          <#list aggregate.stateFields as field>
            <@printTableHeaderCell "${field.name}" "${field.type}" />
          </#list>
          </tr>
        </thead>
        <tbody>
        {items.map(item => (
        <tr key={item.id}>
          <td> <Link to={"/app${aggregate.apiRootPath}/"+item.id}>{item.id} </Link> </td>
          <#list aggregate.stateFields as field>
            <#if field_index != 0>
              <@printTableCell "${field.name}" "${field.type}" />
            </#if>
          </#list>
        </tr>
        ))}
        </tbody>
        </table>
        : <LoadingOrFailed loading={loading}/>
      }
      </div>

      {currentModal}
    </>
  );
};

export default ${fns.capitalize(fns.makePlural(aggregate.aggregateName))};
