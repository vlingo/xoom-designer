<script>
	import { TextField } from "svelte-materialify/src";
	import { requireRule } from "../../validators";

	export let path = "";

	const isPathHaveId = (v) => {
		console.log(v);
		console.log(/^\{id\}/.test(v));
	}
</script>

<TextField bind:value={path} rules={[requireRule]} validateOnBlur={!path}>Path</TextField>