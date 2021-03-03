<script>
	import CardForm from "../components/CardForm.svelte";
	import { persistenceSettings, setLocalStorage } from "../stores";
	import { storageTypes, projectionsTypes, databaseTypes } from '../stores/persistance';
	import { Select, Switch } from "svelte-materialify/src";

	const storageFormat = (val) => $storageTypes.find(t => t.value === val).name;
	const projectionFormat = (val) => $projectionsTypes.find(t => t.value === val).name;
	const databaseFormat = (val) => $databaseTypes.find(t => t.value === val).name;

    let storageType = $persistenceSettings ? $persistenceSettings.storageType : "STATE_STORE";
    let useCQRS = $persistenceSettings ? $persistenceSettings.useCQRS : false;
    let projections = $persistenceSettings ? $persistenceSettings.projections : "NONE";
    let database = $persistenceSettings ? $persistenceSettings.database : "IN_MEMORY";
    let commandModelDatabase = $persistenceSettings ? $persistenceSettings.commandModelDatabase : "IN_MEMORY";
	let queryModelDatabase = $persistenceSettings ? $persistenceSettings.queryModelDatabase : "IN_MEMORY";

	$: changedCQRS(useCQRS);
	$: changedStorageType(storageType);

	function changedCQRS(useCQRS) {
		if(!useCQRS) {
			projections = "NONE";
		} 
	}

	function changedStorageType(storageType) {
		if(storageType === "JOURNAL") {
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event Based', value: 'EVENT_BASED'},
			];
			if(projections === 'OPERATION_BASED') {
				projections = "NONE";
			}
		} else {
			$projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event Based', value: 'EVENT_BASED'},
				{name: 'Operation Based', value: 'OPERATION_BASED'}
			];
		}
	}
	$: $persistenceSettings = { storageType, useCQRS, projections, database, commandModelDatabase, queryModelDatabase }
	$: setLocalStorage("persistenceSettings", $persistenceSettings)
</script>

<svelte:head>
	<title>Persistence</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Persistence" previous="aggregates" next="deployment">
	<Select class="pb-4" mandatory items={$storageTypes} bind:value={storageType} format={(val) => storageFormat(val)}>Storage Type</Select>
	<Switch class="mb-4 pb-4" bind:checked={useCQRS}>Use CQRS</Switch>
	<Select disabled={!useCQRS} class="mb-4 pb-4" mandatory items={$projectionsTypes} bind:value={projections} format={(val) => projectionFormat(val)}>Projections</Select>
	{#if useCQRS}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={commandModelDatabase} format={(val) => databaseFormat(val)}>Command Model Database</Select>
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={queryModelDatabase} format={(val) => databaseFormat(val)}>Query Model Database</Select>
	{:else}
		<Select class="mb-4 pb-4" mandatory items={$databaseTypes} bind:value={database} format={(val) => databaseFormat(val)}>Database</Select>
	{/if}
</CardForm>
