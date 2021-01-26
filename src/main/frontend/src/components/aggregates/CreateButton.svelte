<script>
	import { mdiPlusThick } from "@mdi/js";
	import { Button, Icon } from "svelte-materialify";

	export let title;
	export let fab = true;
	$: icon = !fab;
</script>



<div class="d-flex justify-center align-center">
	<Button {title} {fab} {icon} on:click class="ma-2">
		<Icon path={mdiPlusThick}/>
	</Button>
</div>

<style>
	div {
		width: 100%;
		height: 100%;
	}
</style>