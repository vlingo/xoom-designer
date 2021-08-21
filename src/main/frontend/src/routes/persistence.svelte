<script>
	import CardForm from "../components/CardForm.svelte";
	import VlSelect from "../components/VlSelect.svelte";
	import { settings } from "../stores";
	import { storageTypes, projectionsTypes, databaseTypes } from '../stores/persistence';
	import { Select, Switch } from "svelte-materialify/src";

	let disableCQRS = $settings.model.persistenceSettings.storageType === "JOURNAL";
	const storageFormat = (val) => $storageTypes.find(t => t.value === val).name;
	const projectionFormat = (val) => $projectionsTypes.find(t => t.value === val).name;
	const databaseFormat = (val) => $databaseTypes.find(t => t.value === val).name;

	$: {
		if($settings.model.persistenceSettings.storageType === "JOURNAL") {
			disableCQRS = true;
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event-based', value: 'EVENT_BASED'},
			];
			$settings.model.persistenceSettings = { ...$settings.model.persistenceSettings, useCQRS: true, projections: 'EVENT_BASED' }
		} else {
			disableCQRS = false;
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event-based', value: 'EVENT_BASED'},
				{name: 'Operation-based', value: 'OPERATION_BASED'}
			];
		}
	}

	$: {
		if(!$settings.model.persistenceSettings.useCQRS) {
			$settings.model.persistenceSettings.projections = "NONE";
		}
	}
</script>

<svelte:head>
	<title>Persistence</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Persistence" previous="aggregates" next="deployment">
	<VlSelect class="pb-4" mandatory items={$storageTypes} bind:value={$settings.model.persistenceSettings.storageType} format={(val) => storageFormat(val)}>Storage Type</VlSelect>
	<Switch disabled={disableCQRS} class="mb-4 pb-4" bind:checked={$settings.model.persistenceSettings.useCQRS}>Use CQRS</Switch>
	<Select disabled={!$settings.model.persistenceSettings.useCQRS} class="mb-4 pb-4" mandatory items={$projectionsTypes} bind:value={$settings.model.persistenceSettings.projections} format={(val) => projectionFormat(val)}>Projections</Select>
	{#if $settings.model.persistenceSettings.useCQRS}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$settings.model.persistenceSettings.commandModelDatabase} format={(val) => databaseFormat(val)}>Command Model Database</Select>
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$settings.model.persistenceSettings.queryModelDatabase} format={(val) => databaseFormat(val)}>Query Model Database</Select>
	{:else}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={$settings.model.persistenceSettings.database} format={(val) => databaseFormat(val)}>Database</Select>
	{/if}
</CardForm>
