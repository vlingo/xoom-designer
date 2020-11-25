<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import CardText from "svelte-materialify/src/components/Card/CardText.svelte";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	
	export let title = "";
	export let previous = "";
	export let next = "";
	
</script>

<Card style="display: flex;" class="justify-center align-center flex-column">
	<CardTitle>{title}</CardTitle>
	<CardText>
		<slot/>
	</CardText>
	<CardActions>
		{#if previous}
		<a href={previous} rel="noopener">
			<Button title="Previous">▲</Button>
		</a>
		{/if}
		{#if previous && next}
			<div style="width: 16px"></div>
		{/if}
		{#if next}
		<a href={next} rel="noopener">
			<Button title="Next">▼</Button>
		</a>
		{/if}
	</CardActions>
</Card>