<script>
	import Select from "svelte-materialify/src/components/Select";
	import PathField from "../PathField.svelte";
	import { formatArrayForSelect } from "../../utils";
	import DeleteButton from "./DeleteButton.svelte";
	const httpMethods = formatArrayForSelect(['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS']);

	export let methods;
	export let routes;

	export let path;
	export let httpMethod;
	export let aggregateMethod;
	export let requireEntityLoad;
	export let id;

	let requestMethodDisabled = false;
	$: changedMethodOrMethods(aggregateMethod, methods);
	const changedMethodOrMethods = (_, methods) => {
		const method = methods.find(m => m.name === aggregateMethod);
		console.log(method);
		if(method) {
			if(method.useFactory) {
				httpMethod = "POST";
				requireEntityLoad = false;
				requestMethodDisabled = true;
			} else {
				requireEntityLoad = true;
				requestMethodDisabled = false;
			}
		}
	}
	const deleteRoute = (index) => { routes.splice(index, 1); routes = routes; }
	$: console.log(path, aggregateMethod, requireEntityLoad);
</script>

<span class="d-flex align-center">
	<PathField bind:path={path} bind:requireEntityLoad={requireEntityLoad}/>
	<div style="max-width: 100%">
		<Select mandatory class="ma-2" items={httpMethods} bind:value={httpMethod} disabled={requestMethodDisabled}>Http Request Method</Select>
	</div>
	<Select class="ma-2" items={formatArrayForSelect(methods.map(m => m.name))} bind:value={aggregateMethod}>Aggregate Method</Select>
	<!-- <Switch class="ma-2" bind:checked={requireEntityLoad}>Require Entity Load</Switch> -->
	<DeleteButton title="Delete Route" on:click={() => deleteRoute(id)}/>
</span>