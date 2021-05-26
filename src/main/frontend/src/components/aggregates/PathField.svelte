<script>
	import Textfield from "@smui/textfield/Textfield.svelte";
	import { requireRule } from "../../validators";
	import ErrorWarningTooltip from "./ErrorWarningTooltip.svelte";

	export let path = "";
	export let i;
</script>

<div class="d-flex align-center">
	<Textfield
		id="routePath{i}"
		style="flex: 1;"
		label="Path"
		required
		bind:value={path}
		invalid={[requireRule(path)].some(f => f)}
	/>
	<ErrorWarningTooltip
		type='warning'
		messages={[path.includes('{id}') ? '' : 'Requires {id}?']}
		names={['']}
	/>
</div>