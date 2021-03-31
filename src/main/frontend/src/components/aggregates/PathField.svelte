<script>
	import { TextField } from "svelte-materialify/src";
	import { requireRule } from "../../validators";

	export let path = "";
</script>

<TextField bind:value={path} hint="Requires {'{id}'}?" rules={[requireRule]} validateOnBlur={!path}>Path</TextField>