<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import CardForm from "../components/CardForm.svelte";
	import { modelSettings } from "../stores";

	const storageTypes = [
      {name: "State Store", value: "STATE_STORE"},
      {name: "Journal", value: "JOURNAL"}
	];
	const storageFormat = (val) => storageTypes.find(t => t.value === val).name;
	const databaseTypes = [
      {name: "In Memory", value: "IN_MEMORY"},
      {name: "Postgres", value: "POSTGRES"},
      {name: "HSQLDB", value: "HSQLDB"},
      {name: "MySQL", value: "MYSQL"},
      {name: "YugaByte", value: "YUGA_BYTE"}
    ];


	let aggregates = []; //new Array<Aggregate>()
    let restResources = []; //new Array<Aggregate>()
    let storageType = "STATE_STORE";
    let useCQRS = false;
    let projections = "NONE";
    let database = "IN_MEMORY";
    let commandModelDatabase = "IN_MEMORY";
	let queryModelDatabase = "IN_MEMORY";
	
	const changeType = () => {
		storageType = "JOURNAL"
	}
	$: $modelSettings = { aggregates, restResources, storageType: storageType, useCQRS, projections, database, commandModelDatabase, queryModelDatabase }
	$: console.log($modelSettings);
</script>
<Button on:click={changeType}>JOURNAL</Button>
<!-- add newbie tooltips -->
<CardForm title="Model" previous="context" next="deployment">
	<div class="flex">
		<Card style="flex-grow: 1">
			<CardTitle>
				<Button style="margin: 0 auto">New Aggregate</Button>
			</CardTitle>
		</Card>
		<Card style="flex-grow: 1">
			<CardTitle>
				<Button style="margin: 0 auto">New Domain Event</Button>
			</CardTitle>
		</Card>
		<Card style="flex-grow: 1">
			<Select mandatory>REST Resources</Select>
			<Switch bind:checked={useCQRS}>Use CQRS</Switch>
			<Select mandatory items={storageTypes} bind:value={storageType} format={(val) => storageFormat(val)}>Storage Type</Select>
			<Select mandatory>Domain Model Database</Select>
			<Select mandatory>Command Model Database</Select>
			<Select mandatory>Query Model Database</Select>
			<Select mandatory>Projections</Select>
		</Card>
	</div>
</CardForm>

<style>
	.flex {
		display: flex;
		flex-direction: row;
	}
</style>