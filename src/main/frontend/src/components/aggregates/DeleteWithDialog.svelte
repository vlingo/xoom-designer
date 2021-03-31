<script>
	import {
		Button,
		Card,
		CardActions,
		CardTitle,
		Dialog,
	} from "svelte-materialify/src";
	import DeleteButton from "./DeleteButton.svelte";
	import { createEventDispatcher } from "svelte";
	import Portal from "svelte-portal/src/Portal.svelte";

	const dispatch = createEventDispatcher();
	
	export let type;
	export let color = "red";
	let dialogActive = false;

	const open = () => dialogActive = true;
	const close = () => dialogActive = false;
	const onDelete = () => {
		close();
		setTimeout(() => {
			dispatch('click');
		}, 0);
	}
</script>

<DeleteButton title="Delete {type}" on:click={open} bind:color/>

<Portal target="#portal"> <!-- solves Dialog capturing problem -->
	<Dialog persistent bind:active={dialogActive}>
		<Card class="pa-3">
			<div class="d-flex flex-column">
				<CardTitle>
					Are you sure you want to delete this {type}?
					<div class="small">
						<slot />
					</div>
				</CardTitle>
				<CardActions style="margin-top: auto" class="justify-space-around">
					<Button on:click={onDelete} text class="red-text">Delete</Button>
					<Button on:click={close}>Cancel</Button>
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>
<style lang="scss" global>
	.small {
		font-size: 14px;
		word-break: break-word;
	}
</style>
