<script>
	import { mdiDelete, mdiPlusThick } from "@mdi/js";
	import Button from "svelte-materialify/src/components/Button";
	import Icon from "svelte-materialify/src/components/Icon";
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import Divider from "svelte-materialify/src/components/Divider";
	import CardForm from "../components/CardForm.svelte";
	import PathField from "../components/PathField.svelte";
	import { aggregateSettings } from "../stores";
	import { notEmpty } from "../validators";
	import Dialog from "svelte-materialify/src/components/Dialog";

	function formatArrayForSelect(array) {
		return array.map(
			element => ({ name: element, value: element })
		)
	}

	const stateFieldsTypes =  formatArrayForSelect(['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char']);
	const httpMethods = formatArrayForSelect(['POST', 'PUT', 'DELETE', 'PATCH', 'GET', 'HEAD', 'OPTIONS']);
	let dialogActive = false;

	let aggregateName = "";
	let stateFields = [{ name: "id", type: "String" }];
	let events = [];
	let methods = [];
	let rootPath = "";
	let routes = [];

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
	const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
	const addEvent = () => events = events.concat({ name: "", fields: [] });
	const deleteEvent = (index) => { events.splice(index, 1); events = events; }
	const addMethod = () => methods = methods.concat({ name: "", useFactory: "", parameters: [], event: "" });
	const deleteMethod = (index) => { methods.splice(index, 1); methods = methods; }
	const addRoute = () => routes = routes.concat({ path: "", httpMethod: "", aggregateMethod: "", requireEntityLoad: false });
	const deleteRoute = (index) => { routes.splice(index, 1); routes = routes; }
	
	$: aggregateSetting = { aggregateName, stateFields, events, methods, rootPath, routes }

	const addAggregate = () => {
		$aggregateSettings = [...$aggregateSettings, aggregateSetting];
		dialogActive = !dialogActive;
	}

	$: console.log($aggregateSettings, dialogActive);
</script>

<!-- add newbie tooltips -->
<CardForm title="Aggregates" previous="context" next="persistence">
	<Button on:click={() => dialogActive = !dialogActive}>New Aggregate</Button>
</CardForm>

<Dialog bind:active={dialogActive} width={1000} class="pa-4">
	<TextField bind:value={aggregateName} rules={[notEmpty]} validateOnBlur={!aggregateName}>Aggregate Name</TextField>
	<Divider class="ma-2" />

	<h5>State Fields:</h5>
	{#each stateFields as stateField, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={stateField.name}>Name</TextField>
			<Select class="ma-2" items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
			<Button on:click={() => deleteStateField(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button fab on:click={addStateField} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<Divider class="ma-2" />

	<h5>Events:</h5>
	{#each events as event, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={event.name}>Name</TextField>
			<Select multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={event.fields}>Fields</Select>
			<Button on:click={() => deleteEvent(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button fab on:click={addEvent} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<Divider class="ma-2" />

	<h5>Methods:</h5>
	{#each methods as method, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={method.name}>Name</TextField>
			<Switch bind:checked={method.useFactory}>Use Factory</Switch>
			<Select multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={method.parameters}>Parameters</Select>
			<Select multiple class="ma-2" items={formatArrayForSelect(events.map(e => e.name))} bind:value={method.event}>Event</Select>
			<Button on:click={() => deleteMethod(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button fab on:click={addMethod} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<Divider class="ma-2" />

	<h5>API:</h5>
	<TextField class="ma-2" bind:value={rootPath}>Root Path</TextField>
	{#each routes as route, i}
		<span class="d-flex align-center">
			<PathField bind:path={route.path} bind:requireEntityLoad={route.requireEntityLoad}/>
			<Select class="ma-2" items={httpMethods} bind:value={route.httpMethod}>Http Request Method</Select>
			<Select class="ma-2" items={formatArrayForSelect(methods.map(m => m.name))} bind:value={route.aggregateMethod}>Aggregate Method</Select>
			<Switch class="ma-2" bind:checked={route.requireEntityLoad}>Require Entity Load</Switch>
			<Button on:click={() => deleteRoute(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button fab on:click={addRoute} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<Divider class="ma-2" />
	<Button on:click={addAggregate}>Add</Button>
	<Button on:click={() => dialogActive = !dialogActive}>Cancel</Button>
</Dialog>