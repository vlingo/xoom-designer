<script>
	import { afterUpdate, createEventDispatcher } from 'svelte';
	import { Select } from "svelte-materialify/src";
	import PathField from "./PathField.svelte";
	import { formatArrayForSelect } from "../../utils";
	import DeleteButton from "./DeleteButton.svelte";
	const httpMethods = formatArrayForSelect(['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS']);

	export let methods;
	export let route;

	let requestMethodDisabled = false;
	$: changedMethodOrMethods(route.aggregateMethod, methods);
	const changedMethodOrMethods = (aggregateMethod, methods) => {
		const method = methods.find(m => m.name === aggregateMethod);
		if(method) {
			if(method.useFactory) {
				httpMethod = "POST";
				route.requireEntityLoad = false;
				requestMethodDisabled = true;
			} else {
				route.requireEntityLoad = true;
				requestMethodDisabled = false;
			}
		}
	}

	const dispatch = createEventDispatcher();

	const deleteRoute = () => {
		dispatch('delete')
	};

	afterUpdate(() => {
		route.aggregateMethod = methods.some(method => method.name === route.aggregateMethod) ? route.aggregateMethod : undefined;
	});
</script>

<div class="d-flex align-center">
	<div class="mb-3 pb-3 mr-4" style="flex: 1;">
		<PathField bind:path={route.path} />
	</div>
	<div class="mb-3 pb-3 mr-4" style="flex: 1;">
		<Select mandatory items={httpMethods} bind:value={route.httpMethod} disabled={requestMethodDisabled}>Http Request Method</Select>
	</div>
	<div class="mb-3 pb-3" style="flex: 1;">
		<Select mandatory items={formatArrayForSelect(methods.map(m => m.name))} bind:value={route.aggregateMethod}>Aggregate Method</Select>
	</div>
	<!-- <Switch class="ma-2" bind:checked={route.requireEntityLoad}>Require Entity Load</Switch> -->
	<div style="width: 36px;">
		<DeleteButton title="Delete Route" on:click={deleteRoute}/>
	</div>
</div>