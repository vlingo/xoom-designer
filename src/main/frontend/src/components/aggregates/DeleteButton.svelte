<script>
	import { mdiDelete } from "@mdi/js";
	import Button from "svelte-materialify/src/components/Button";
	import Icon from "svelte-materialify/src/components/Icon";
	
	export let title;
	export let color = "red";

	
</script>


<Button {title} on:click icon class="ma-2 {color}-text">
	<Icon path={mdiDelete}/>
</Button>



