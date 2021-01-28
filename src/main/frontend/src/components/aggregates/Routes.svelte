<script>
	import { TextField } from 'svelte-materialify/src';
  import { requireRule, routeRule } from "../../validators";
	import CreateButton from "./CreateButton.svelte";

  import Route from "./Route.svelte";
  export let rootPath;
  export let routes;
  export let methods;

  const addRoute = () => routes = routes.concat({ path: "", httpMethod: "GET", aggregateMethod: "", requireEntityLoad: false });

</script>
<h5>API:</h5>
<TextField class="ma-2" bind:value={rootPath} rules={[requireRule, routeRule]} validateOnBlur={!rootPath}>Root Path</TextField>

{#each routes as { path, httpMethod, aggregateMethod, requireEntityLoad } , id}
  <Route bind:path bind:httpMethod bind:aggregateMethod bind:requireEntityLoad {id} bind:methods bind:routes/>
{/each}
<CreateButton title="Add Route" on:click={addRoute}/>