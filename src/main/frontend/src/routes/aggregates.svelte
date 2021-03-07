<script>
	import CardForm from "../components/CardForm.svelte";
	import AggregateCard from "../components/aggregates/AggregateCard.svelte";
	import AggregateDialog from "../components/aggregates/AggregateDialog.svelte";
	import { aggregateSettings, setLocalStorage } from "../stores";
	import { SlideGroup, SlideItem, Icon } from "svelte-materialify/src";
	import { mdiPlus } from "@mdi/js";
import Button from "svelte-materialify/src/components/Button";

	let dialogActive = false;
	let editMode = false;

	let currentId;

	const newAggregate = () => {
		currentId = $aggregateSettings.length;
		dialogActive = true;
	}

	const edit = (id) => {
		currentId = id;
		dialogActive = true;
		editMode = true;
	}

	const remove = (id) => {
		$aggregateSettings.splice(id, 1);
		$aggregateSettings = $aggregateSettings;
	}

	$: if(!dialogActive && editMode) editMode = false;

	$: setLocalStorage("aggregateSettings", $aggregateSettings)
</script>

<svelte:head>
	<title>Aggregates</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Aggregates" previous="object-values" next="persistence">
	<Button class="mb-4" hover on:click={newAggregate}>
		<div title="Add Aggregate" class="d-flex align-center justify-center">
			<Icon class="black-text mr-4" path={mdiPlus}/>
			New Aggregate
		</div>
	</Button>
	<div class="d-flex">
		<SlideGroup activeClass="white-text">
			<SlideItem>
				{#each $aggregateSettings as aggregate, id}
					<AggregateCard {aggregate} on:edit={() => edit(id)} on:remove={() => remove(id)}/>
				{/each}
			</SlideItem>
		</SlideGroup>
	</div>
</CardForm>

{#if dialogActive}
	<AggregateDialog bind:dialogActive bind:editMode bind:currentId/>
{/if}