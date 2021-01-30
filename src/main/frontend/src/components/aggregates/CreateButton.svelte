<script>
	import { mdiPlusThick } from "@mdi/js";
	import { Button, Icon } from "svelte-materialify/src";

	export let title;
	export let fab = true;
	$: icon = !fab;
</script>



<div class="vl-btn d-flex justify-center align-center">
	<Button {title} {fab} {icon} on:click>
		<Icon path={mdiPlusThick}/>
	</Button>
</div>

<style>
	:global(fieldset) { 
		position: relative;
	}
	.vl-btn {
		position: absolute;
    bottom: -14px;
    right: -14px;
    display: flex;
    width: auto;
    height: auto;
	}
</style>