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

<Card>
	<CardTitle>{title}</CardTitle>
	<CardText>
		<slot/>
	</CardText>
	<CardActions>
		{#if previous}
			<Button on:click={goToPrevious}>▲ Previous</Button>
		{/if}
		{#if next}
			<Button on:click={goToNext}>▼ Next</Button>
		{/if}
	</CardActions>
</Card>