<script>
	import Button from "svelte-materialify/src/components/Button";
	import Icon from "svelte-materialify/src/components/Icon";
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import { mdiDelete } from "@mdi/js";

	export let stateFields;
	export let events;
	export let method;
	export let id;
	
	const deleteMethod = (index) => { methods.splice(index, 1); methods = methods; }
	$: console.log(method);
</script>

<span class="d-flex">
	<TextField class="ma-2" bind:value={method.name}>Name</TextField>
	<Switch bind:checked={method.useFactory}>Use Factory</Switch>
	<Select multiple class="ma-2" items={stateFields} bind:value={method.parameters}>Parameters</Select>
	<Select class="ma-2" items={events} bind:value={method.event}>Event</Select>
	<Button title="Delete Method" on:click={() => deleteMethod(id)} icon class="ma-2 red-text">
		<Icon path={mdiDelete}/>
	</Button>
</span>