<script>
  import Textfield from '@smui/textfield/Textfield.svelte';
  import { requireRule, routeRule } from "../../validators";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';
  import Route from "./Route.svelte";
  import { tick } from 'svelte';

  export let rootPath;
  export let routes;
  export let methods;

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
</script>

<FieldsetBox title="API" on:add={addRoute}>
  <div class="mb-4 d-flex align-center">
    <Textfield
      style="width: 100%;"
      label="Root Path"
      required
      bind:value={rootPath}
      invalid={[requireRule(rootPath), routeRule(rootPath)].some(f => f)}
    />
    <ErrorWarningTooltip
      names={['Root path', 'Root path']}
      messages={[requireRule(rootPath), routeRule(rootPath)]}
    />
  </div>

  {#each routes as route, i}
    <Route bind:route bind:methods {i} on:delete={() => deleteRoute(i)}/>
  {/each}
</FieldsetBox>