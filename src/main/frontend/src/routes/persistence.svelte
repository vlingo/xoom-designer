<script>
	import CardForm from "../components/CardForm.svelte";
	import VlSelect from "../components/VlSelect.svelte";
	import { persistenceSettings } from "../stores";
	import { storageTypes, projectionsTypes, databaseTypes } from '../stores/persistence';
	import { Select, Switch } from "svelte-materialify/src";

	const storageFormat = (val) => $storageTypes.find(t => t.value === val).name;
	const projectionFormat = (val) => $projectionsTypes.find(t => t.value === val).name;
	const databaseFormat = (val) => $databaseTypes.find(t => t.value === val).name;

	function onStorageTypeChange(value) {
		if (value.detail === 'JOURNAL') {
			$persistenceSettings = { ...$persistenceSettings, useCQRS: true, projections: 'EVENT_BASED' }
		}
	}

	$: {
		if($persistenceSettings.storageType === "JOURNAL") {
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event-based', value: 'EVENT_BASED'},
			];
		} else {
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event-based', value: 'EVENT_BASED'},
				{name: 'Operation-based', value: 'OPERATION_BASED'}
			];
		}
	}

	$: {
		if(!$persistenceSettings.useCQRS) {
			$persistenceSettings.projections = "NONE";
		}
	}
</script>

<svelte:head>
	<title>Persistence</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Persistence" previous="aggregates" next="deployment">
	<VlSelect class="pb-4" mandatory items={$storageTypes} bind:value={$persistenceSettings.storageType} format={(val) => storageFormat(val)} on:change={onStorageTypeChange}>Storage Type</VlSelect>
	<Switch class="mb-4 pb-4" bind:checked={$persistenceSettings.useCQRS}>Use CQRS</Switch>
	<Select disabled={!$persistenceSettings.useCQRS} class="mb-4 pb-4" mandatory items={$projectionsTypes} bind:value={$persistenceSettings.projections} format={(val) => projectionFormat(val)}>Projections</Select>
	{#if $persistenceSettings.useCQRS}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$persistenceSettings.commandModelDatabase} format={(val) => databaseFormat(val)}>Command Model Database</Select>
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$persistenceSettings.queryModelDatabase} format={(val) => databaseFormat(val)}>Query Model Database</Select>
	{:else}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$persistenceSettings.database} format={(val) => databaseFormat(val)}>Database</Select>
	{/if}
</CardForm>
