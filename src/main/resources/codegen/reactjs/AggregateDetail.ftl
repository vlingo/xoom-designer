import {useParams} from 'react-router-dom';
import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import LoadingOrFailed from "../LoadingOrFailed";
<#list aggregate.methods as method>
  <#if !method.useFactory >
import ${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(method.name)} from "./${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(method.name)}";
  </#if>
</#list>
<#macro printTableRow name type label>
  <#if valueTypes[type]??>
    <#list valueTypes[type] as subType>
      <@printTableRow "${name}?.${subType.name}" subType.type "${name} ${subType.name}"/>
    </#list>
  <#else>
    <#if type=="LocalDate">
      {!Array.isArray(item?.${name?split("?.")[0]}) && <tr><td>${fns.capitalizeMultiWord(label)}</td><td>{new Intl.DateTimeFormat('en-GB', {
        month: 'long',
        day: '2-digit',
        year: 'numeric',
        }).format(new Date(item?.${name}))}</td></tr>}
    <#else>
      {!Array.isArray(item?.${name?split("?.")[0]}) && <tr><td>${fns.capitalizeMultiWord(label)}</td><td>{item?.${name}}</td></tr>}
    </#if>
  </#if>
</#macro>
<#macro printItemsTable name type>
  <table className={'table table-striped table-bordered'}>
  <thead>
  <tr>
    <#if valueTypes[type]??>
      <@printTableHeaderCell "${name}" "${type}" />
    <#else>
      <th>${fns.capitalizeMultiWord(name)}</th>
    </#if>
  </tr>
  </thead>
  <tbody>
  {item?.${name}.map(item => (
  <tr>
      <#if valueTypes[type]??>
        <@printItemTableCell "${name}" "${type}" />
        <#else>
        <td>{item}</td>
      </#if>
  </tr>
  ))}
  </tbody>
  </table>
</#macro>
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
    <td>{item?.${name}}</td>
  </#if>
</#macro>
<#macro printItemTableCell name type>
  <#if valueTypes[type]??>
    <#list valueTypes[type] as subType>
      <@printItemTableCell "${subType.name}" subType.type/>
    </#list>
  <#else>
    <td>{item?.${name}}</td>
  </#if>
</#macro>

const ${fns.capitalize(aggregate.aggregateName)} = () => {

  const [loading, setLoading] = useState(false);
  const {id} = useParams();
  const [item, setItem] = useState(null);
  const [currentModal, setCurrentModal] = useState(null);

  const loadItem = useCallback((id) => {
    axios.get('${aggregate.apiRootPath}/'+id)
      .then(res => res.data)
      .then(data => {
        console.log('${aggregate.aggregateName} axios success', data);
        setItem(data);
      })
      .catch(e => {
        console.error('${aggregate.aggregateName} axios failed', e);
      })
      .finally(() => {
        setLoading(false);
      })
  }, []);

  const onModalActionComplete = useCallback((data) => {
    if (data){
      setItem((item) => {
        return {...item, ...data};
      });
    }
    setCurrentModal(null);
  }, []);

  <#list aggregate.methods as method>
    <#if !method.useFactory >
  const _${method.name} = useCallback((e) => {
    console.log('showing ${method.name} modal');
    const form = {
      id: item.id,
      <#list method.parameters as p>
      ${p}: item.${p}<#if p?has_next>,</#if>
      </#list>
    };
    setCurrentModal(<${aggregate.aggregateName}${fns.capitalize(method.name)} id={id} defaultForm={form} complete={onModalActionComplete}/>);
  }, [id, item, onModalActionComplete]);
    </#if>
  </#list>

  useEffect(() => {
    setLoading(true);
    loadItem(id);
  }, [id, loadItem]);

  return (
    <div>
      {
      item
      ?
      <>
      <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 className="h2">${aggregate.aggregateName}</h1>
        <div className="btn-toolbar mb-2 mb-md-0">
          <div className="btn-group me-2">
            <#list aggregate.methods as method>
              <#if !method.useFactory >
            <button type="button" className="btn btn-sm btn-outline-secondary" onClick={_${method.name}}>${fns.capitalize(method.name)}</button>
              </#if>
            </#list>
          </div>
        </div>
      </div>
      <div>
        <table className={'table'}>
          <tbody>
          <#list aggregate.stateFields as field>
              <@printTableRow "${field.name}" "${field.type}" "${field.name}"/>
          </#list>
          </tbody>
        </table>
        <#list aggregate.stateFields as field>
          <#if field.isCollection>
            <@printItemsTable "${field.name}" "${field.type}"/>
          </#if>
        </#list>

      </div>
    </>
    :
    <LoadingOrFailed loading={loading}/>
      }

      {currentModal}
    </div>
  );
};

export default ${fns.capitalize(aggregate.aggregateName)};
