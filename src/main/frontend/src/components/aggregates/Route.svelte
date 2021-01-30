<script>
	import { Select } from "svelte-materialify/src";
	import PathField from "./PathField.svelte";
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
	const changedMethodOrMethods = (aggregateMethod, methods) => {
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

<div class="d-flex align-center">
	<div class="mb-3 pb-3 mr-4" style="flex: 1;">
		<PathField bind:path={path} bind:requireEntityLoad={requireEntityLoad}/>
	</div>
	<div class="mb-3 pb-3 mr-4" style="flex: 1;">
		<Select mandatory items={httpMethods} bind:value={httpMethod} disabled={requestMethodDisabled}>Http Request Method</Select>
	</div>
	<div class="mb-3 pb-3" style="flex: 1;">
		<Select mandatory items={formatArrayForSelect(methods.map(m => m.name))} bind:value={aggregateMethod}>Aggregate Method</Select>
	</div>
	<!-- <Switch class="ma-2" bind:checked={requireEntityLoad}>Require Entity Load</Switch> -->
	<div style="width: 36px;">
		<DeleteButton title="Delete Route" on:click={() => deleteRoute(id)}/>
	</div>
</div>