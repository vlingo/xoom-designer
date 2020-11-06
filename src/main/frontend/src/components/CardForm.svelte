<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import CardText from "svelte-materialify/src/components/Card/CardText.svelte";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	import { goto } from '@sapper/app';
	
	export let title = "";
	export let previous = "";
	export let next = "";
	
	const goToPrevious = () => previous && goto(previous);
	const goToNext = () => next && goto(next);
</script>

<Card style="display: flex;" class="justify-center align-center flex-column">
	<CardTitle>{title}</CardTitle>
	<CardText>
		<slot/>
	</CardText>
	<CardActions>
		{#if previous}
		<div title="Previous">
			<Button on:click={goToPrevious}>▲</Button>
		</div>
		{/if}
		{#if previous && next}
			<div style="width: 16px"></div>
		{/if}
		{#if next}
		<div title="Next">
			<Button on:click={goToNext}>▼</Button>
		</div>
		{/if}
	</CardActions>
</Card>