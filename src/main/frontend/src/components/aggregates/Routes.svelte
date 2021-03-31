<script>
	import { TextField } from 'svelte-materialify/src';
  import { requireRule, routeRule } from "../../validators";
	import CreateButton from "./CreateButton.svelte";

  import Route from "./Route.svelte";
  export let rootPath;
  export let routes;
  export let methods;

  const addRoute = () => routes = routes.concat({ path: "", httpMethod: "GET", aggregateMethod: "", requireEntityLoad: false });
  const deleteRoute = (index) => { routes.splice(index, 1); routes = routes; }
  $: {
    routes = [...routes]
  }

</script>
<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">API</h6>
  </legend>

  <TextField class="mb-3 pb-3" bind:value={rootPath} rules={[requireRule, routeRule]} validateOnBlur={!rootPath}>Root Path</TextField>

  {#each routes as { path, httpMethod, aggregateMethod, requireEntityLoad } , id}
    <Route bind:path bind:httpMethod bind:aggregateMethod bind:requireEntityLoad bind:methods on:delete={() => deleteRoute(id)}/>
  {/each}
  <CreateButton title="Add Route" on:click={addRoute}/>
</fieldset>