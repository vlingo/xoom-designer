<script>
	import { Dialog, CardActions } from 'svelte-materialify/src';
	import { settings, getLocalStorage, setLocalStorage, resolveDefaultExchangeName } from "../../stores";
	import { classNameRule, identifierRule, requireRule, rootPathRule, routeRule, isPropertyUniqueRule, isAggregateUniqueRule, schemaGroupRule, schemaRule, methodParametersValidityWithSelectedEventRule, eventAlreadyInUseRule } from "../../validators";
	import StateFields from './StateFields.svelte';
	import Events from './Events.svelte';
	import Methods from './Methods.svelte';
	import Routes from './Routes.svelte';
	import ProducerExchange from './ProducerExchange.svelte';
	import ConsumerExchange from './ConsumerExchange.svelte';
	import ValueObjects from './ValueObjects.svelte';
	import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
	import { uuid } from '../../utils';
	import Textfield from '@smui/textfield';
	import { Button } from '../ui';
	import { onMount } from 'svelte';

	export let dialogActive;
	export let editMode;
	export let oldAggregate;
	let newAggregate;
	let aggregateName, stateFields, events, methods, rootPath, producerExchangeName, consumerExchangeName, schemaGroup, disableSchemaGroup, routes, outgoingEvents, receivers;
	let aggregateNameElement;
	let routesComponent, routesHasErrorMessages;

	onMount(() => {
		aggregateNameElement.focus();
	})

	const defaultExchangeName = resolveDefaultExchangeName($settings);
	const retrieveSchemaGroup = () => $settings.model.aggregateSettings.length > 0 ? $settings.model.aggregateSettings[0].producerExchange.schemaGroup : "";
	const canWriteSchemaGroup = () => (schemaGroup == undefined || schemaGroup.length == 0); //currentId == 0 ||
	const initialAggregate = {
		aggregateName: "",
		stateFields: [{ name: "id", type: "String", collectionType: null, uid: uuid() }],
		events: [],
		methods: [],
		api: {
			rootPath: "/",
			routes: [],
		},
		producerExchange: {
			exchangeName: defaultExchangeName,
			schemaGroup: retrieveSchemaGroup(),
			outgoingEvents: [],
		},
		consumerExchange: {
			exchangeName: defaultExchangeName,
			receivers: [],
		},
		disableSchemaGroup: !canWriteSchemaGroup(),
	}

	const add = () => {
		if(!valid) return;
		$settings.model.aggregateSettings = [...$settings.model.aggregateSettings, removeUIDsFromStateFields(newAggregate)];
		closeDialog();
	}

	const update = () => {
		if(!valid) return;
		$settings.model.aggregateSettings = $settings.model.aggregateSettings.filter(a => JSON.stringify(a) !== JSON.stringify(oldAggregate));
		$settings.model.aggregateSettings = [...$settings.model.aggregateSettings, removeUIDsFromStateFields(newAggregate)];
		closeDialog();
	}

	function removeUIDsFromStateFields(aggregate) {
		const agg = { ...aggregate }
		if (aggregate.stateFields) {
			agg.stateFields = aggregate.stateFields && aggregate.stateFields.map(({ uid, ...f }) => (f))
		}
		return agg;
	}

	const closeDialog = () => {
		clearFields();
		dialogActive = false;
		setLocalStorage("aggregateDialogState", {});
	}

	const initFieldsWith = (aggregate) => {
		aggregateName = aggregate.aggregateName;
		stateFields = aggregate.stateFields && aggregate.stateFields.map((f) => ({ ...f, uid: uuid() }));
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
  const isExistInValueObject = (value, array, prop) => !isPropertyUniqueRule(value, array, prop);

	const validField = (f) => !identifierRule(f.name) && f.type && !isPropertyUniqueRule(f.name, stateFields, 'name');
	const validEvent = (e) => !classNameRule(e.name) && e.fields.length > 0 && !isPropertyUniqueRule(e.name, events, 'name') && !isExistInValueObject(e.name, $settings.model.valueObjectSettings, 'name');
	const validMethod = (m) => !identifierRule(m.name) && !isPropertyUniqueRule(m.name, methods, 'name') && !eventAlreadyInUseRule(m, methods);
	const validRoute = (r) => r.path && !routeRule(r.path) && r.aggregateMethod;
	const validProducer = (schema, events) => (schema && !schemaGroupRule(schema) && events.length > 0) || (!schema && events.length === 0);
	const validConsumer = (receivers) => (receivers.length === 0) || (receivers.length > 0 && receivers.every(r => r.schema && !schemaRule(r.schema) && r.aggregateMethod));
	const validRootPathRule = (rootPath) => (!rootPathRule(rootPath) && (routesComponent && routesComponent.isValidRootPath(rootPath)));

	$: {
		const storageState = getLocalStorage("aggregateDialogState");
		if(storageState && storageState.newAggregate) initFieldsWith(storageState.newAggregate);
		/* this changes if editMode OR oldAggregate changes (so even if you enter with editMode multiple times, it will react) */
		else editMode ? initFieldsWith(oldAggregate) : clearFields();
	}

	$: valid = !classNameRule(aggregateName) && stateFields.every(validField) && events.every(validEvent) && methods.every(validMethod)
	&& validRootPathRule(rootPath) && routes.every(validRoute) && !isAggregateUniqueRule(oldAggregate, aggregateName, $settings.model.aggregateSettings) && validProducer(schemaGroup, outgoingEvents) && validConsumer(receivers);
	$: if(valid) {
		newAggregate = {
			aggregateName, stateFields, events, methods, api: { rootPath, routes }
			, producerExchange: { "exchangeName" : producerExchangeName, schemaGroup, outgoingEvents }
			, consumerExchange: {  "exchangeName" : consumerExchangeName, receivers }
		};
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
	<div class="d-flex mb-4 align-center">
		<Textfield
			bind:this={aggregateNameElement}
			style="flex: 1;"
			label="Aggregate Name"
			required
			bind:value={aggregateName}
			invalid={[requireRule(aggregateName), classNameRule(aggregateName), isAggregateUniqueRule(oldAggregate, aggregateName, $settings.model.aggregateSettings)].some(f => f)}
		>
		</Textfield>
		<ErrorWarningTooltip
			messages={[requireRule(aggregateName), classNameRule(aggregateName), isAggregateUniqueRule(oldAggregate, aggregateName, $settings.model.aggregateSettings)]}
			names={['Aggregate Name', 'Aggregate Name', 'Aggregate Name']}
		/>
	</div>
	<ValueObjects bind:events />
	<StateFields bind:stateFields />
	<Events bind:events bind:stateFields bind:valueObjects={$settings.model.valueObjectSettings}/>
	<Methods bind:methods bind:stateFields bind:events />
	<Routes bind:this={routesComponent} bind:routes bind:methods bind:rootPath bind:stateFields/>
	<ProducerExchange bind:events bind:producerExchangeName bind:outgoingEvents bind:schemaGroup bind:disableSchemaGroup  />
	<ConsumerExchange bind:consumerExchangeName bind:receivers bind:methods />
	<CardActions>
		<Button variant="raised" color="primary" disabled={!valid} on:click={editMode ? update : add}>
			{editMode ? 'Update' : 'Add'}
		</Button>
		<span style="flex: 1;"></span>
		<Button variant="outlined" color="secondary" class="mr-4" on:click={clearFields}>Clear Fields</Button>
		<Button variant="outlined" color="secondary" on:click={closeDialog}>Cancel</Button>
	</CardActions>
</Dialog>