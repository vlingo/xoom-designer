<script>
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import CardForm from "../components/CardForm.svelte";
	import { persistenceSettings } from "../stores";

	const storageTypes = [
      {name: "State Store", value: "STATE_STORE"},
      {name: "Journal", value: "JOURNAL"}
	];
	const storageFormat = (val) => storageTypes.find(t => t.value === val).name;
	let projectionsTypes = [
		{name: 'Not Applicable', value: 'NONE'},
		{name: 'Event Based', value: 'EVENT_BASED'},
		{name: 'Operation Based', value: 'OPERATION_BASED'}
	];
	const projectionFormat = (val) => projectionsTypes.find(t => t.value === val).name;
	const databaseTypes = [
      {name: "In Memory", value: "IN_MEMORY"},
      {name: "Postgres", value: "POSTGRES"},
      {name: "HSQLDB", value: "HSQLDB"},
      {name: "MySQL", value: "MYSQL"},
      {name: "YugaByte", value: "YUGA_BYTE"}
    ];
	const databaseFormat = (val) => databaseTypes.find(t => t.value === val).name;

    let storageType = "STATE_STORE";
    let useCQRS = false;
    let projections = "NONE";
    let database = "IN_MEMORY";
    let commandModelDatabase = "IN_MEMORY";
	let queryModelDatabase = "IN_MEMORY";
	
	$: changedStorageType(storageType);
	function changedStorageType(storageType) {
		if(storageType === "JOURNAL") {
			projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event Based', value: 'EVENT_BASED'},
			];
			projections = "NONE";
		} else {
			projectionsTypes = [
				{name: 'Not Applicable', value: 'NONE'},
				{name: 'Event Based', value: 'EVENT_BASED'},
				{name: 'Operation Based', value: 'OPERATION_BASED'}
			];
		}
	}
	$: $persistenceSettings = { storageType, useCQRS, projections, database, commandModelDatabase, queryModelDatabase }
	$: console.log($persistenceSettings);
</script>

<svelte:head>
	<title>Persistence</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Persistence" previous="aggregates" next="deployment">
	<Select class="ma-4" mandatory items={storageTypes} bind:value={storageType} format={(val) => storageFormat(val)}>Storage Type</Select>
	<Select class="ma-4" mandatory items={projectionsTypes} bind:value={projections} format={(val) => projectionFormat(val)}>Projections</Select>
	<Switch class="ma-4" bind:checked={useCQRS}>Use CQRS</Switch>
	{#if useCQRS}
		<Select class="ma-4" mandatory items={databaseTypes} bind:value={commandModelDatabase} format={(val) => databaseFormat(val)}>Command Model Database</Select>
		<Select class="ma-4" mandatory items={databaseTypes} bind:value={queryModelDatabase} format={(val) => databaseFormat(val)}>Query Model Database</Select>
	{:else}
		<Select class="ma-4" mandatory items={databaseTypes} bind:value={database} format={(val) => databaseFormat(val)}>Database</Select>
	{/if}
</CardForm>