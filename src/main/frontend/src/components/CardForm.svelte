<script>
	import { goto } from '@sapper/app';
	import { Button, Card, CardActions, CardText, CardTitle, Icon } from 'svelte-materialify/src';
	import { mdiArrowLeft, mdiArrowRight } from "@mdi/js";
	export let title = "";
	export let previous = "";
	export let next = "";
	export let valid = true;
</script>
<Card
	style="display: flex; box-shadow: 0 0 64px 16px rgba(0, 0, 0, 0.075); border-radius: 16px;"
	class="vl-card__shadow justify-center align-center flex-column pa-4 pa-md-8">
	<CardTitle class="pa-0">
		<h4 class="mb-0" style="font-weight: bold;">{title}</h4>
	</CardTitle>
	<CardText class="pa-0 mt-6 mb-6">
		<slot/>
	</CardText>
	<CardActions class="justify-center pa-0">
		{#if previous}
			<div class="mr-4">
				<Button outlined class="success-text" on:click={goto(previous)} title="Previous">
					<Icon path={mdiArrowLeft} />
					<span class="ml-2 d-none d-sm-block">{previous}</span>
			</Button>
			</div>
		{/if}
		{#if next}
			<div class="ml-4" style="flex: 1; text-align: right;">
				<Button outlined class="{!valid ? 'disabled' : 'success-text'}" on:click={goto(next)} disabled={!valid} title="Next">
					<span class="mr-2 d-none d-sm-block">{next}</span>
					<Icon path={mdiArrowRight}/>
				</Button>
			</div>
		{/if}
	</CardActions>
</Card>