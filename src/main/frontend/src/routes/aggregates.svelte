<script>
	import CardForm from "../components/CardForm.svelte";
	import AggregateCard from "../components/aggregates/AggregateCard.svelte";
	import AggregateDialog from "../components/aggregates/AggregateDialog.svelte";
	import { aggregateSettings } from "../stores";
	import Card from "svelte-materialify/src/components/Card";
	import CreateButton from "../components/aggregates/CreateButton.svelte";

	let dialogActive = false;
	let editMode = false;

	let currentId;

	const newAggregate = () => {
		console.log($aggregateSettings.length);
		currentId = $aggregateSettings.length;
		dialogActive = true;
	}

	const edit = (id) => {
		console.log(id);
		currentId = id;
		dialogActive = true;
		editMode = true;
	}

	const remove = (id) => {
		$aggregateSettings.splice(id, 1);
		$aggregateSettings = $aggregateSettings;
	}

	$: if(!dialogActive && editMode) editMode = false;

	$: console.log($aggregateSettings);
</script>

<!-- add newbie tooltips -->
<CardForm title="Aggregates" previous="context" next="persistence">
	<div class="d-flex flex-wrap">
		<Card class="pa-3 ma-3" style="width: 15rem; height: 20rem">
			<CreateButton fab={false} title="Add Aggregate" on:click={newAggregate}/>
		</Card>
		{#each $aggregateSettings as aggregate, id}
			<AggregateCard {aggregate} on:edit={() => edit(id)} on:remove={() => remove(id)}/>
		{/each}
	</div>
</CardForm>

{#if currentId !== undefined}
	<AggregateDialog bind:dialogActive bind:editMode bind:currentId/>
{/if}