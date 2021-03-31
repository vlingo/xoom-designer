<script>
	import { TextField } from "svelte-materialify/src";
	import { requireRule, pathShouldHaveId } from "../../validators";

	export let path = "";
</script>

<TextField bind:value={path} rules={[requireRule, pathShouldHaveId]} validateOnBlur={!path}>Path</TextField>