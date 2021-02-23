<script>
	import { mdiDelete } from "@mdi/js";
	import { Button, Icon} from "svelte-materialify/src";
	
	export let title;
	export let color = "red";

	
</script>


<Button {title} on:click icon class="ml-2 {color}-text">
	<Icon path={mdiDelete}/>
</Button>



