<script>
  import Textfield from '@smui/textfield/Textfield.svelte';
  import { requireRule, rootPathRule } from "../../validators";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';
  import Route from "./Route.svelte";
  import { tick } from 'svelte';

  export let rootPath;
  export let routes;
  export let methods;
  export let stateFields;

  const addRoute = () => {
    routes = [...routes, { path: "", httpMethod: "GET", aggregateMethod: "", requireEntityLoad: false }]
    tick().then(() => {
      const el = document.querySelector(`#routePath${routes.length - 1} input`);
      if (el) el.focus()
    })
  };
  const deleteRoute = (index) => {
    routes.splice(index, 1);
    routes = [...routes];
    tick().then(() => {
      const el = document.querySelector(`#routePath${index === 0 ? 0 : index - 1} input`);
      if (el) el.focus()
    })
  }

  function resolvePathVariableMessage(rootPath) {
    return rootPath.includes("{") && !pathVariablesMatchCompositeIdFields(pathVariablesFrom(rootPath)) ?
      "Path variables name in the root path need to match Composite Id state fields name." : "";
  }

  const pathVariablesMatchCompositeIdFields = (pathVariables) => {
    return stateFields
	    .filter(field => field.type == "CompositeId")
	    .map(field => field.name)
	    .every(name => pathVariables.includes(name));
  }

  const pathVariablesFrom = (rootPath) => {
		const regex = /\{(.*?)\}/gm;
		let matches;
		let result = [];

		while ((matches = regex.exec(rootPath)) !== null) {
	    if (matches.index === regex.lastIndex) {
	        regex.lastIndex++;
	    }

	    matches.forEach((match, groupIndex) => {
				result.push(match)
	    });
		}
    return result;
  }

  function resolveErrorMessages() {
    return [requireRule(rootPath), rootPathRule(rootPath), resolvePathVariableMessage(rootPath)];
  }

  function hasErrorMessages() {
    const errorMessages = resolveErrorMessages();
    return errorMessages.some(m => m && m.length > 0);
  }

  export function isValidRootPath(rootPath) {
    return resolvePathVariableMessage(rootPath).length == 0;
  }
</script>

<FieldsetBox title="API" on:add={addRoute}>
  <div class="mb-4 d-flex align-center">
    <Textfield
      style="width: 100%;"
      label="Root Path"
      required
      bind:value={rootPath}
      invalid={[requireRule(rootPath), rootPathRule(rootPath), resolvePathVariableMessage(rootPath)].some(f => f)}
    />
    <ErrorWarningTooltip
      type={'error'}
      names={hasErrorMessages() ? ['Root path', 'Root path', ''] : ['', '', '']}
      messages={hasErrorMessages() ? [requireRule(rootPath), rootPathRule(rootPath), resolvePathVariableMessage(rootPath)] : [undefined]}
    />
  </div>

  {#each routes as route, i}
    <Route bind:route bind:methods {i} on:delete={() => deleteRoute(i)}/>
  {/each}
</FieldsetBox>