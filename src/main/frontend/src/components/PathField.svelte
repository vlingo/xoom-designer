<script>
	import TextField from "svelte-materialify/src/components/TextField";

	export let path = "";
	export let requireEntityLoad = false;

	$: changed(requireEntityLoad);
	function changed(requireEntityLoad) {
		if(requireEntityLoad) {
			path = "/{id}/"+path;
		} else {
			path = path.substring(6);
		}
	}

	const changedPath = (e) => {
		if(requireEntityLoad && !e.target.value.includes("/{id}/")) path = "/{id}/";
	}
	
</script>

<TextField class="ma-2" bind:value={path} on:input={changedPath}>Path</TextField>