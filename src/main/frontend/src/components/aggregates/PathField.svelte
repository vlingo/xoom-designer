<script>
	import { TextField } from "svelte-materialify/src";
	import { requireRule } from "../../validators";
	import ErrorWarningTooltip from "./ErrorWarningTooltip.svelte";

	export let path = "";
</script>

<div class="d-flex">
	<TextField bind:value={path} rules={[requireRule]} validateOnBlur={!path}>Path</TextField>
	<ErrorWarningTooltip
		type='warning'
		messages={['Requires {id}?']}
		names={['']}
	/>
</div>