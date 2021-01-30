<script>
	import {
		Button,
		TextField,
		Dialog,
		CardActions
	} from 'svelte-materialify/src';
	import { aggregateSettings, currentAggregate, setLocalStorage } from "../../stores";
	import { classNameRule, identifierRule, requireRule, routeRule, isPropertyUnique } from "../../validators";

	import StateFields from './StateFields.svelte';
	import Events from './Events.svelte';
	import Methods from './Methods.svelte';
	import Routes from './Routes.svelte';
	import ProducerExchange from './ProducerExchange.svelte';
	import ConsumerExchange from './ConsumerExchange.svelte';

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

	$: valid = !classNameRule(aggregateName) && stateFields.every(validField) && events.every(validEvent) && methods.every(validMethod) && !routeRule(rootPath) && routes.every(validRoute) && !isPropertyUnique(aggregateName, [...$aggregateSettings, { aggregateName }], 'aggregateName');
	$: if(valid) {
		$currentAggregate = { aggregateName, stateFields, events, methods, api: { rootPath, routes }, producerExchange: { "exchangeName" : producerExchangeName, schemaGroup, outgoingEvents }, consumerExchange: {  "exchangeName" : consumerExchangeName, receivers } };
		//TODO: rework this - we need to keep the modal open, too.
		setLocalStorage("currentAggregate", $currentAggregate);
	}
</script>

<Dialog bind:active={dialogActive} width={1000} class="pa-4 pa-lg-8 rounded">
	<h4 class="mb-5" style="text-align: center;">
		{#if editMode}
			Update Aggregate
		{:else}
			New Aggregate
		{/if}
	</h4>
	<TextField class="mb-4" bind:value={aggregateName} rules={[requireRule, classNameRule, (v) => isPropertyUnique(v, [...$aggregateSettings, { aggregateName }], 'aggregateName')]} validateOnBlur={!aggregateName}>Aggregate Name</TextField>
	<!-- <Divider class="ma-2" /> -->
	<StateFields bind:stateFields />
	<!-- <Divider class="ma-2" /> -->
	<Events bind:events  bind:stateFields />
	<!-- <Divider class="ma-2" /> -->
	<Methods bind:methods bind:stateFields bind:events />
	<!-- <Divider class="ma-2" /> -->
	<Routes bind:routes bind:methods bind:rootPath />
	<!-- <Divider class="ma-2" /> -->
	<ProducerExchange bind:events bind:producerExchangeName bind:outgoingEvents bind:schemaGroup bind:disableSchemaGroup  />

	<ConsumerExchange bind:consumerExchangeName bind:receivers bind:methods />

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