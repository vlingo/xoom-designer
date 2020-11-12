<script>
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import DeleteButton from "./DeleteButton.svelte";

	export let stateFields;
	export let events;
	export let methods;
	export let method;
	export let id;
	
	const deleteMethod = (index) => { methods.splice(index, 1); methods = methods; }
	$: console.log(method);
</script>

<div class="d-flex align-center">
	<div>
		<span class="d-flex">
			<TextField class="ma-2" bind:value={method.name}>Name</TextField>
			<Select multiple class="ma-2" items={stateFields} bind:value={method.parameters}>Parameters</Select>
			<Select class="ma-2" items={events} bind:value={method.event}>Event</Select>
		</span>
		<div>
			<Switch bind:checked={method.useFactory}>Involves creation of entity?</Switch>
		</div>
	</div>
	<DeleteButton title="Delete Method" on:click={() => deleteMethod(id)}/>
</div>