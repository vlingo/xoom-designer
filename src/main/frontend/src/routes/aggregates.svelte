<script>
	import CardForm from "../components/CardForm.svelte";
	import AggregateCard from "../components/aggregates/AggregateCard.svelte";
	import AggregateDialog from "../components/aggregates/AggregateDialog.svelte";
	import { aggregateSettings, getLocalStorage, setLocalStorage } from "../stores";
	import { SlideGroup, SlideItem, Icon } from "svelte-materialify/src";
	import { mdiPlus } from "@mdi/js";
	import Button from "svelte-materialify/src/components/Button";

	let dialogActive = getLocalStorage("aggregateDialogState") ? getLocalStorage("aggregateDialogState").dialogActive : false;
	let editMode = getLocalStorage("aggregateDialogState") ? getLocalStorage("aggregateDialogState").editMode : false;

	let oldAggregate = getLocalStorage("aggregateDialogState") ? getLocalStorage("aggregateDialogState").oldAggregate : undefined;

	const newAggregate = () => {
		dialogActive = true;
		editMode = false;
	}

	const edit = (aggregate) => {
		oldAggregate = aggregate;
		dialogActive = true;
		editMode = true;
	}

	const remove = (aggregate) => {
		$aggregateSettings = $aggregateSettings.filter(a => JSON.stringify(a) !== JSON.stringify(aggregate));
	}

	$: setLocalStorage("aggregateSettings", $aggregateSettings)
</script>

<svelte:head>
	<title>Aggregates</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Aggregates" previous="context" next="persistence">
	<Button class="mb-4" hover on:click={newAggregate}>
		<div title="Add Aggregate" class="d-flex align-center justify-center">
			<Icon class="black-text mr-4" path={mdiPlus}/>
			New Aggregate
		</div>
	</Button>
	<div class="d-flex" style="overflow-x: auto;">
		{#each $aggregateSettings as aggregate}
			<AggregateCard {aggregate} on:edit={() => edit(aggregate)} on:remove={() => remove(aggregate)}/>
		{/each}
	</div>
</CardForm>

{#if dialogActive}
	<AggregateDialog bind:dialogActive bind:editMode bind:oldAggregate/>
{/if}