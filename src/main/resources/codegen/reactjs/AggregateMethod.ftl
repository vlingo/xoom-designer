import FormModal from "../FormModal";
import useFormHandler from "../../utils/FormHandler";
import {useCallback} from "react";
import axios from "axios";
import {EMPTY_FORM} from "./${fns.capitalize(fns.makePlural(aggregate.aggregateName))}";
<#macro printFormElement name type>
    <#if valueTypes[type]??>
        <#list valueTypes[type] as subType>
            <@printFormElement "${name}?.${subType.name}" subType.type/>
        </#list>
    <#else>
      <div className='mb-3'>
        <label htmlFor='${name?replace('?', '')}' className={'form-label text-capitalize'}>${fns.capitalizeMultiWord(name?replace('?.', ' '))}</label>
        <input id='${name?replace('?', '')}' name={'${name?replace('?', '')}'} required={true} value={form.${name}} onChange={onFormValueChange} className={'form-control form-control-sm'}/>
      </div>
    </#if>
</#macro>

<#macro formatFactoryFormElement name type isCollection>
  <#if name?contains(".") && isCollection>
    if(!Array.isArray(form.${name}))
      form.${name} = [form.${name}]
  </#if>
  <#if valueTypes[type]??>
    <#if !name?contains("[0]") && isCollection>
    if(Array.isArray(EMPTY_FORM.${name}) && !Array.isArray(form.${name}))
      form.${name} = [form.${name}]
    </#if>
    <#list valueTypes[type] as subType>
      <#if isCollection>
        <@formatFactoryFormElement "${name}[0].${subType.name}" subType.type subType.isCollection/>
      <#else>
        <@formatFactoryFormElement "${name}.${subType.name}" subType.type subType.isCollection/>
      </#if>
    </#list>
  <#elseif !name?contains(".") && !name?contains("[0]") && isCollection>
    form.${name} = [form.${name}]
  </#if>
</#macro>
<#macro formatFormElement name type isCollection>
  <#if name?contains(".") && isCollection>
    if(!Array.isArray(form.${name}))
      form.${name} = [form.${name}]
    <#elseif isCollection>
    if(Array.isArray(form.${name}) && form.${name}.length === 0)
      form.${name} = [Object.assign({}, form.${name})]
  </#if>
  <#if valueTypes[type]??>
    <#list valueTypes[type] as subType>
      <#if isCollection>
        <@formatFormElement "${name}[0].${subType.name}" subType.type subType.isCollection/>
      <#else>
        <@formatFormElement "${name}.${subType.name}" subType.type subType.isCollection/>
      </#if>
    </#list>
  <#elseif !name?contains("[0]") && isCollection>
    form.${name} = [form.${name}]
  </#if>
</#macro>

const applyData = (uri, data) => {
  return uri.replace(/(?:{(.+?)})/g, x => data[x.slice(1,-1)]);
}

const ${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(method.name)} = ({id = null, defaultForm, complete}) => {

  const [form, onFormValueChange] = useFormHandler(defaultForm);

  const submit = useCallback((e) => {
<#list method.parameters as p>
  <#if method.useFactory>
    <@formatFactoryFormElement p fieldTypes[p] aggregate.stateFields?filter(field -> field.name == p && field.isCollection)?has_content/>
  <#else>
    <@formatFormElement p fieldTypes[p] aggregate.stateFields?filter(field -> field.name == p && field.isCollection)?has_content/>
  </#if>
</#list>

    const url = applyData('${route.path}', form);
<#if route.httpMethod?length == 0>
    axios.post(url, form)
  <#else>
    axios.${route.httpMethod?lower_case}(url, form)
</#if>
    .then(res => res.data)
    .then(data => {
      complete(data);
      console.log('${aggregate.aggregateName}${fns.capitalize(method.name)} axios complete', data);
    })
    .catch(e => {
      console.error('${aggregate.aggregateName}${fns.capitalize(method.name)} axios failed', e);
    });
  }, [form, complete]);

  const close = useCallback((e) => {
    complete();
  }, [complete]);

  return (
    <>
      <FormModal title={"${fns.capitalize(method.name)}"} show={true} close={close} submit={submit}>
        <#list method.parameters as p>
          <@printFormElement p fieldTypes[p] />
        </#list>
      </FormModal>
    </>
  )
};

export default ${fns.capitalize(aggregate.aggregateName)}${fns.capitalize(method.name)};
