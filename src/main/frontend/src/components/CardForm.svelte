<script>
	import { Button, Card, CardActions, CardText, CardTitle } from 'svelte-materialify/src';
	
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