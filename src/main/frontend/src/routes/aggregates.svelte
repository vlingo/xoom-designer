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
	import Method from "../components/aggregates/Method.svelte";
	import Route from "../components/aggregates/Route.svelte";
	import { formatArrayForSelect } from "../utils";

	const stateFieldsTypes =  formatArrayForSelect(['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char']);
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
	const addMethod = () => methods = methods.concat({ name: "", useFactory: false, parameters: [], event: "" });
	const addRoute = () => routes = routes.concat({ path: "", httpMethod: "", aggregateMethod: "", requireEntityLoad: false });
	
	$: aggregateSetting = { aggregateName, stateFields, events, methods, api: { rootPath, routes } }

	const addAggregate = () => {
		$aggregateSettings = [...$aggregateSettings, aggregateSetting];
		dialogActive = !dialogActive;
	}

	$: console.log(aggregateSetting);
	$: console.log($aggregateSettings);
</script>

<!-- add newbie tooltips -->
<CardForm title="Aggregates" previous="context" next="persistence">
	<Button on:click={() => dialogActive = !dialogActive}>New Aggregate</Button>
</CardForm>

<Dialog bind:active={dialogActive} width={1000} class="pa-4">
	<h4 style="text-align: center;">New Aggregate</h4>
	<TextField bind:value={aggregateName} rules={[notEmpty]} validateOnBlur={!aggregateName}>Aggregate Name</TextField>
	<!-- <Divider class="ma-2" /> -->

	<h5>State Fields:</h5>
	{#each stateFields as stateField, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={stateField.name}>Name</TextField>
			<Select class="ma-2" items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
			<Button title="Delete State Field" on:click={() => deleteStateField(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button title="Add State Field" fab on:click={addStateField} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<!-- <Divider class="ma-2" /> -->

	<h5>Events:</h5>
	{#each events as event, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={event.name}>Name</TextField>
			<Select multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={event.fields}>Fields</Select>
			<Button title="Delete Event" on:click={() => deleteEvent(i)} icon class="ma-2 red-text">
				<Icon path={mdiDelete}/>
			</Button>
		</span>
	{/each}
	<div class="d-flex justify-center">
		<Button title="Add Event" fab on:click={addEvent} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<!-- <Divider class="ma-2" /> -->

	<h5>Methods:</h5>
	{#each methods as method, id}
		<Method bind:method {id} stateFields={formatArrayForSelect(stateFields.map(f => f.name))} events={formatArrayForSelect(events.map(e => e.name))}/>
	{/each}
	<div class="d-flex justify-center">
		<Button title="Add Method" fab on:click={addMethod} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<!-- <Divider class="ma-2" /> -->

	<h5>API:</h5>
	<TextField class="ma-2" bind:value={rootPath}>Root Path</TextField>

	{#each routes as { path, httpMethod, aggregateMethod, requireEntityLoad } , id}
		<Route bind:path bind:httpMethod bind:aggregateMethod bind:requireEntityLoad {id} bind:methods bind:routes/>
	{/each}
	<div class="d-flex justify-center">
		<Button title="Add Route" fab on:click={addRoute} class="ma-2">
			<Icon path={mdiPlusThick}/>
		</Button>
	</div>
	<!-- <Divider class="ma-2" /> -->
	<Button on:click={addAggregate}>Add</Button>
	<Button on:click={() => dialogActive = !dialogActive}>Cancel</Button>
</Dialog>