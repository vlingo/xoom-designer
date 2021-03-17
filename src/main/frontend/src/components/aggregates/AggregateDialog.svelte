<script>
	import { Button, TextField, Dialog, CardActions } from 'svelte-materialify/src';
	import { aggregateSettings, getLocalStorage, setLocalStorage } from "../../stores";
	import { classNameRule, identifierRule, requireRule, routeRule, isPropertyUniqueRule, isAggregateUniqueRule } from "../../validators";
	import StateFields from './StateFields.svelte';
	import Events from './Events.svelte';
	import Methods from './Methods.svelte';
	import Routes from './Routes.svelte';
	import ProducerExchange from './ProducerExchange.svelte';
	import ConsumerExchange from './ConsumerExchange.svelte';
	import ValueObjects from './ValueObjects.svelte';

	export let dialogActive;
	export let editMode;
	export let oldAggregate;
	let newAggregate;
	let aggregateName, stateFields, events, methods, rootPath, producerExchangeName, consumerExchangeName, schemaGroup, disableSchemaGroup, routes, outgoingEvents, receivers;


	const retrieveSchemaGroup = () => $aggregateSettings.length > 0 ? $aggregateSettings[0].producerExchange.schemaGroup : "";
	const canWriteSchemaGroup = () => (schemaGroup == undefined || schemaGroup.length == 0); //currentId == 0 ||
	const initialAggregate = {
		aggregateName: "",
		stateFields: [{ name: "id", type: "String" }],
		events: [],
		methods: [],
		api: {
			rootPath: "/",
			routes: [],
		},
		producerExchange: {
			exchangeName: "",
			schemaGroup: retrieveSchemaGroup(),
			outgoingEvents: [],
		},
		consumerExchange: {
			exchangeName: "",
			receivers: [],
		},
		disableSchemaGroup: !canWriteSchemaGroup(),
	}

	const add = () => {
		if(!valid) return;
		$aggregateSettings = [...$aggregateSettings, newAggregate];
		closeDialog();
	}

	const update = () => {
		if(!valid) return;
		$aggregateSettings = $aggregateSettings.filter(a => JSON.stringify(a) !== JSON.stringify(oldAggregate));
		$aggregateSettings = [...$aggregateSettings, newAggregate];
		closeDialog();
	}

	const closeDialog = () => {
		clearFields();
		dialogActive = false;
		setLocalStorage("aggregateDialogState", {});
	}

	const initFieldsWith = (aggregate) => {
		aggregateName = aggregate.aggregateName;
		stateFields = aggregate.stateFields;
		events = aggregate.events;
		methods = aggregate.methods;
		rootPath = aggregate.api.rootPath;
		routes = aggregate.api.routes;
		producerExchangeName = aggregate.producerExchange.exchangeName;
		schemaGroup = aggregate.producerExchange.schemaGroup;
		outgoingEvents = aggregate.producerExchange.outgoingEvents;
		consumerExchangeName = aggregate.consumerExchange.exchangeName;
		receivers = aggregate.consumerExchange.receivers;
		disableSchemaGroup = aggregate.disableSchemaGroup;
	}
	const clearFields = () => initFieldsWith(initialAggregate);

	const validField = (f) => !identifierRule(f.name) && f.type && !isPropertyUniqueRule(f.name, stateFields, 'name');
	const validEvent = (e) => !classNameRule(e.name) && e.fields.length > 0 && !isPropertyUniqueRule(e.name, events, 'name');
	const validMethod = (m) => !identifierRule(m.name) && m.parameters.length > 0 && m.event && !isPropertyUniqueRule(m.name, methods, 'name');
	const validRoute = (r) => r.path && r.aggregateMethod;

	/* this changes if editMode OR oldAggregate changes (so even if you enter with editMode multiple times, it will react) */
	$: editMode ? initFieldsWith(oldAggregate) : clearFields();
	$: valid = !classNameRule(aggregateName) && stateFields.every(validField) && events.every(validEvent) && methods.every(validMethod) 
	&& !routeRule(rootPath) && routes.every(validRoute) && !isAggregateUniqueRule(oldAggregate, aggregateName, $aggregateSettings);
	
	$: if(valid) {
		newAggregate = {aggregateName, stateFields, events, methods, api: { rootPath, routes }, producerExchange: { "exchangeName" : producerExchangeName, schemaGroup, outgoingEvents }, consumerExchange: {  "exchangeName" : consumerExchangeName, receivers } };
		//TODO: rework this - we need to keep the modal open, too.
		setLocalStorage("aggregateDialogState", {oldAggregate, newAggregate, dialogActive, editMode});
	}
</script>

<Dialog bind:active={dialogActive} persistent width={1000} class="pa-4 pa-lg-8 rounded">
	<h4 class="mb-5" style="text-align: center;">
		{#if editMode}
			Update Aggregate
		{:else}
			New Aggregate
		{/if}
	</h4>
	<TextField class="mb-4" bind:value={aggregateName} rules={[requireRule, classNameRule, (name) => isAggregateUniqueRule(oldAggregate, name, $aggregateSettings)]} validateOnBlur={!aggregateName}>Aggregate Name</TextField>
	<ValueObjects />
	<StateFields bind:stateFields />
	<Events bind:events  bind:stateFields />
	<Methods bind:methods bind:stateFields bind:events />
	<Routes bind:routes bind:methods bind:rootPath />
	<ProducerExchange bind:events bind:producerExchangeName bind:outgoingEvents bind:schemaGroup bind:disableSchemaGroup  />
	<ConsumerExchange bind:consumerExchangeName bind:receivers bind:methods />
	<CardActions>
		{#if editMode}
			<Button class="mr-3" on:click={update} disabled={!valid}>Update</Button> <!--TODO: vlingo colors: class="primary-color", right now materialify overwrites disabled state-->
		{:else}
			<Button class="mr-3" on:click={add} disabled={!valid}>Add</Button> <!--TODO: vlingo colors-->
		{/if}
		<Button outlined on:click={closeDialog}>Cancel</Button>
		<span style="width: 100%;"></span>
		<Button outlined class="red red-text" on:click={clearFields}>Clear Fields</Button>
	</CardActions>
</Dialog>