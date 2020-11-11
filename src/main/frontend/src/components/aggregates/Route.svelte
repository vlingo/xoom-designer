<script>
	import Button from "svelte-materialify/src/components/Button";
	import Icon from "svelte-materialify/src/components/Icon";
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import PathField from "../PathField.svelte";
	import { mdiDelete } from "@mdi/js";
	import { formatArrayForSelect } from "../../utils";
	const httpMethods = formatArrayForSelect(['POST', 'PUT', 'DELETE', 'PATCH', 'GET', 'HEAD', 'OPTIONS']);

	export let methods;
	export let routes;

	export let path;
	export let httpMethod;
	export let aggregateMethod;
	export let requireEntityLoad;
	export let id;

	let requestMethodDisabled;
	$: changedMethodOrMethods(aggregateMethod, methods);
	const changedMethodOrMethods = (m, methods) => {
		const method = methods.find(m => m.name === aggregateMethod);
		if(method && method.useFactory) {
			httpMethod = "POST";
			requestMethodDisabled = true;
		} else {
			requestMethodDisabled = false;
		}
	}
	const deleteRoute = (index) => { routes.splice(index, 1); routes = routes; }
	$: console.log(path, aggregateMethod, requireEntityLoad);
</script>

<span class="d-flex align-center">
	<PathField bind:path={path} bind:requireEntityLoad={requireEntityLoad}/>
	<Select class="ma-2" items={httpMethods} bind:value={httpMethod} disabled={requestMethodDisabled}>Http Request Method</Select>
	<Select class="ma-2" items={formatArrayForSelect(methods.map(m => m.name))} bind:value={aggregateMethod}>Aggregate Method</Select>
	<Switch class="ma-2" bind:checked={requireEntityLoad}>Require Entity Load</Switch>
	<Button title="Delete Route" on:click={() => deleteRoute(id)} icon class="ma-2 red-text">
		<Icon path={mdiDelete}/>
	</Button>
</span>