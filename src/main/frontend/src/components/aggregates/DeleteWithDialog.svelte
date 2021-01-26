<script>
	import {
		Button,
		Card,
		CardActions,
		CardTitle,
		Dialog,
	} from "svelte-materialify";
	import DeleteButton from "./DeleteButton.svelte";
	
	export let type;
	let dialogActive = false;

	const open = () => dialogActive = true;
	const close = () => dialogActive = false;
</script>

<DeleteButton title="Delete {type}" on:click={open} color=""/>
	
<Dialog persistent bind:active={dialogActive}>
	<Card class="pa-3">
		<div class="d-flex flex-column" style="height: 8rem">
			<CardTitle>Do you really want to delete this {type}?</CardTitle>
			<CardActions style="margin-top: auto" class="justify-space-around">
				<Button on:click text class="red-text">Delete</Button>
				<Button on:click={close}>Cancel</Button>
			</CardActions>
		</div>
	</Card>
</Dialog>
