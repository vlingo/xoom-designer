<script>
	import Button from "svelte-materialify/src/components/Button";
	import Select from "svelte-materialify/src/components/Select";
	import TextField from "svelte-materialify/src/components/TextField";
	import Dialog from "svelte-materialify/src/components/Dialog";
	import Method from "./Method.svelte";
	import Route from "./Route.svelte";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import { aggregateSettings } from "../../stores";
	import { classNameRule, identifierRule, requireRule, routeRule } from "../../validators";
	import { formatArrayForSelect } from "../../utils";
	import DeleteButton from "./DeleteButton.svelte";
	import CreateButton from "./CreateButton.svelte";

	const stateFieldsTypes =  formatArrayForSelect(['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char']);
	export let dialogActive;
	export let editMode;

	export let currentId;
	
	let aggregateName = $currentAggregate ? $currentAggregate.aggregateName : "";
	let stateFields = [{ name: "id", type: "String" }];
	let events = [];
	let methods = [];
	let rootPath = "/";
	let producerExchangeName = "";
	let consumerExchangeName = "";
	let schemaGroup = "";
	let disableSchemaGroup = false;
	let routes = [];
	let outgoingEvents = [];
	let receivers = [];

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
	const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
	const addEvent = () => events = events.concat({ name: "", fields: ["id"] });
	const deleteEvent = (index) => { events.splice(index, 1); events = events; }
	const addMethod = () => methods = methods.concat({ name: "", useFactory: false, parameters: [], event: "" });
	const addRoute = () => routes = routes.concat({ path: "", httpMethod: "GET", aggregateMethod: "", requireEntityLoad: false });
	const addReceiver = () => {
		if(receivers.length > 0) {
			const lastReceiver = receivers[receivers.length-1];
			const schemaPrefix = lastReceiver.schema.split(":").splice(0, 3).join(":");
			const schemaPlaceholder = schemaPrefix + ":[Enter the schema name]:0.0.1";
			receivers = receivers.concat({ aggregateMethod: "", schema: schemaPlaceholder})
		} else {
			receivers = receivers.concat({ aggregateMethod: "", schema: "" })
		}
	}
	const deleteReceiver = (index) => { receivers.splice(index, 1); receivers = receivers; }


	const add = () => {
		if(requireRule(aggregateName)) return;
		$aggregateSettings = [...$aggregateSettings, $currentAggregate];
		currentId = undefined;
		reset();
		dialogActive = false;
	}

	const update = () => {
		if(requireRule(aggregateName)) return;
		$aggregateSettings.splice(currentId, 1, $currentAggregate);
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
		producerExchangeName = "";
		schemaGroup = retrieveSchemaGroup();
		disableSchemaGroup = !canWriteSchemaGroup();
		outgoingEvents = [];
		consumerExchangeName = "";
		receivers = [];
	}

	const retrieveSchemaGroup = () => {
		return $aggregateSettings.length > 0 ? $aggregateSettings[0].producerExchange.schemaGroup : "";
	}

	const canWriteSchemaGroup = () => {
		return currentId == 0 || (schemaGroup == undefined && schemaGroup.length == 0);
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

	const validField = (f) => !identifierRule(f.name) && f.type;
	const validEvent = (e) => !classNameRule(e.name) && e.fields.length > 0;
	const validMethod = (m) => !identifierRule(m.name) && m.parameters.length > 0 && m.event;
	const validRoute = (r) => r.path && r.aggregateMethod;

	$: valid = !classNameRule(aggregateName) && stateFields.every(validField) && events.every(validEvent) && methods.every(validMethod) && !routeRule(rootPath) && routes.every(validRoute);
	$: if(valid) {
		$currentAggregate = { aggregateName, stateFields, events, methods, api: { rootPath, routes }, producerExchange: { "exchangeName" : producerExchangeName, schemaGroup, outgoingEvents }, consumerExchange: {  "exchangeName" : consumerExchangeName, receivers } };
		//TODO: rework this - we need to keep the modal open, too.
		setLocalStorage("currentAggregate", $currentAggregate);
	}
	$: console.log($currentAggregate);
	$: console.log(currentId);
</script>

<Dialog bind:active={dialogActive} width={1000} class="pa-4">
	<h4 style="text-align: center;">
		{#if editMode}
			Update Aggregate
		{:else}
			New Aggregate
		{/if}
	</h4>
	<TextField bind:value={aggregateName} rules={[requireRule, classNameRule]} validateOnBlur={!aggregateName}>Aggregate Name</TextField>
	<!-- <Divider class="ma-2" /> -->

	<h5>State Fields:</h5>
	{#each stateFields as stateField, i}
		<span class="d-flex">
		<div style="max-width: 100%">
			<TextField disabled={i<1} class="ma-2" bind:value={stateField.name} rules={[requireRule, identifierRule]}>Name</TextField>
		</div>
		<div style="max-width: 100%">
			<Select mandatory disabled={i<1} class="ma-2" items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
		</div>
		{#if i>0}
			<DeleteButton title="Delete State Field" on:click={() => deleteStateField(i)}/>
		{/if}
		</span>
	{/each}
	<CreateButton title="Add State Field" on:click={addStateField}/>
	<!-- <Divider class="ma-2" /> -->

	<h5>Events:</h5>
	{#each events as event, i}
		<span class="d-flex">
			<TextField class="ma-2" bind:value={event.name} rules={[requireRule, classNameRule]} validateOnBlur={!event.name}>Name</TextField>
			<Select mandatory disabled={!stateFields.length} multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={event.fields}>Fields</Select>
			<DeleteButton title="Delete Event" on:click={() => deleteEvent(i)}/>
		</span>
	{/each}
	<CreateButton title="Add Event" on:click={addEvent}/>
	<!-- <Divider class="ma-2" /> -->

	<h5>Methods:</h5>
	{#each methods as method, id}
		<Method bind:method {id} stateFields={formatArrayForSelect(stateFields.map(f => f.name))} events={formatArrayForSelect(events.map(e => e.name))} bind:methods/>
	{/each}
	<CreateButton title="Add Method" on:click={addMethod}/>
	<!-- <Divider class="ma-2" /> -->

	<h5>API:</h5>
	<TextField class="ma-2" bind:value={rootPath} rules={[requireRule, routeRule]} validateOnBlur={!rootPath}>Root Path</TextField>

	{#each routes as { path, httpMethod, aggregateMethod, requireEntityLoad } , id}
		<Route bind:path bind:httpMethod bind:aggregateMethod bind:requireEntityLoad {id} bind:methods bind:routes/>
	{/each}
	<CreateButton title="Add Route" on:click={addRoute}/>
	<!-- <Divider class="ma-2" /> -->
	
	<CardActions>
		{#if editMode}
			<Button class="mr-3" on:click={update} disabled={!valid}>Update</Button>
		{:else}
			<Button class="mr-3" on:click={add} disabled={!valid}>Add</Button>
		{/if}
		<Button on:click={() => dialogActive = !dialogActive}>Cancel</Button>
		<span style="width: 100%;"></span>
		<Button on:click={reset}>Reset</Button>
	</CardActions>
</Dialog>