<script>
	import Button from "svelte-materialify/src/components/Button";
	import Icon from "svelte-materialify/src/components/Icon";
	import Select from "svelte-materialify/src/components/Select";
	import TextField from "svelte-materialify/src/components/TextField";
	import Dialog from "svelte-materialify/src/components/Dialog";
	import Method from "./Method.svelte";
	import Route from "./Route.svelte";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import { mdiDelete, mdiPlusThick } from "@mdi/js";
	import { aggregateSettings } from "../../stores";
	import { notEmpty } from "../../validators";
	import { formatArrayForSelect } from "../../utils";

	const stateFieldsTypes =  formatArrayForSelect(['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char']);
	export let dialogActive;
	export let editMode;

	export let currentId;
	
	let aggregateName = "";
	let stateFields = [{ name: "id", type: "String" }];
	let events = [];
	let methods = [];
	let rootPath = "/";
	let routes = [];

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
	const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
	const addEvent = () => events = events.concat({ name: "", fields: [] });
	const deleteEvent = (index) => { events.splice(index, 1); events = events; }
	const addMethod = () => methods = methods.concat({ name: "", useFactory: false, parameters: [], event: "" });
	const addRoute = () => routes = routes.concat({ path: "", httpMethod: "", aggregateMethod: "", requireEntityLoad: false });

	const add = () => {
		$aggregateSettings = [...$aggregateSettings, aggregateSetting];
		currentId = undefined;
		reset();
		dialogActive = false;
	}

	const update = () => {
		$aggregateSettings.splice(currentId, 1, aggregateSetting);
		$aggregateSettings = $aggregateSettings;
		currentId = undefined;
		reset();
		dialogActive = false;
	}

	const reset = () => {
		aggregateName = "";
		stateFields = [{ name: "id", type: "String" }];
		events = [];
		methods = [];
		rootPath = "/";
		routes = [];
	}

	$: changedCurrent(currentId);
	function changedCurrent(id) {
		console.log(id, $aggregateSettings[id]);
		if(id !== undefined && $aggregateSettings[id]) {
			const aggregateWithId =  $aggregateSettings[id];
			aggregateName = aggregateWithId.aggregateName;
			stateFields = aggregateWithId.stateFields;
			events = aggregateWithId.events;
			methods = aggregateWithId.methods;
			rootPath = aggregateWithId.api.rootPath;
			routes = aggregateWithId.api.routes;
		} else {
			reset();
		}
	}

	$: aggregateSetting = { aggregateName, stateFields, events, methods, api: { rootPath, routes } };
	$: console.log(aggregateSetting);
	$: console.log(currentId);
</script>

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
		<Method bind:method {id} stateFields={formatArrayForSelect(stateFields.map(f => f.name))} events={formatArrayForSelect(events.map(e => e.name))} bind:methods/>
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
	<CardActions>
		{#if editMode}
			<Button class="mr-3" on:click={update}>Update</Button>
		{:else}
			<Button class="mr-3" on:click={add}>Add</Button>
		{/if}
		<Button on:click={() => dialogActive = !dialogActive}>Cancel</Button>
		<span style="width: 100%;"></span>
		<Button on:click={reset}>Reset</Button>
	</CardActions>
</Dialog>