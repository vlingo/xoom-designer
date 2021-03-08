<script>
	import { mdiDelete } from "@mdi/js";
	import { Button, Icon} from "svelte-materialify/src";
	
	export let title;
	export let color = "red";

	
</script>


<Button {title} on:click icon class="{color}-text">
	<Icon path={mdiDelete}/>
</Button>



