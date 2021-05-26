<script>
	import { afterUpdate, createEventDispatcher } from 'svelte';
	import PathField from "./PathField.svelte";
	import DeleteButton from "./DeleteButton.svelte";
	import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
	import { requireRule } from '../../validators';
	import Select, { Option } from '@smui/select';

	const httpMethods = ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS'];

	export let methods;
	export let route;
	export let i;

	let requestMethodDisabled = false;
	$: changedMethodOrMethods(route.aggregateMethod, methods);
	const changedMethodOrMethods = (aggregateMethod, methods) => {
		const method = methods.find(m => m.name === aggregateMethod);
		if(method) {
			if(method.useFactory) {
				route.httpMethod = "POST";
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
		<PathField bind:path={route.path} {i} />
	</div>
	<div class="mb-3 pb-3 mr-4" style="flex: 1;">
		<Select
			bind:value={route.httpMethod}
			required
			label="Http Request Method"
		>
			{#each httpMethods as method}
				<Option value={method}>{method}</Option>
			{/each}
		</Select>
	</div>
	<div class="mb-3 pb-3" style="flex: 1;">
		<Select
			bind:value={route.aggregateMethod}
			required
			label="Aggregate Method"
		>
			{#each methods.filter(m => m.name) as method}
				<Option value={method.name}>{method.name}</Option>
			{/each}
		</Select>
	</div>
	<div>
    <ErrorWarningTooltip
      names={['Root path', 'Http Request Method', 'Aggregate Method']}
      messages={[requireRule(route.path), requireRule(route.httpMethod), requireRule(route.aggregateMethod)]}
    />
	</div>
	<!-- <Switch class="ma-2" bind:checked={route.requireEntityLoad}>Require Entity Load</Switch> -->
	<div style="width: 36px;">
		<DeleteButton title="Delete Route" on:click={deleteRoute}/>
	</div>
</div>