<script>
	import { TextField } from 'svelte-materialify/src';
  import { requireRule, routeRule } from "../../validators";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';

  import Route from "./Route.svelte";
  export let rootPath;
  export let routes;
  export let methods;

  const addRoute = () => {
    routes = [...routes, { path: "", httpMethod: "GET", aggregateMethod: "", requireEntityLoad: false }]
  };
  const deleteRoute = (index) => {
    routes.splice(index, 1);
    routes = [...routes];
  }
</script>

<FieldsetBox title="API" on:add={addRoute}>
  <div>
    <TextField class="mb-3 pb-3" bind:value={rootPath} rules={[requireRule, routeRule]} validateOnBlur={!rootPath}>Root Path</TextField>
    <ErrorWarningTooltip
      names={['Root path', 'Root path']}
      messages={[requireRule(rootPath), routeRule(rootPath)]}
    />
  </div>

  {#each routes as route, i}
    <Route bind:route bind:methods on:delete={() => deleteRoute(i)}/>
  {/each}
</FieldsetBox>