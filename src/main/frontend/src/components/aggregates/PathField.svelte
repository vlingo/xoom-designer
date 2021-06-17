<script>
	import Textfield from "@smui/textfield/Textfield.svelte";
	import { requireRule, routeRule } from "../../validators";
	import ErrorWarningTooltip from "./ErrorWarningTooltip.svelte";

	export let path = "";
	export let i;

	function resolveIdPathParamMessage() {
		return path.includes('{id}') ? '' : 'Requires {id}?';
	}

	function resolveRootPathSymbolMessage() {
		return path.includes('*') ? '' : 'Use * if the path is the same as the root path.';
	}

</script>

<div class="d-flex align-center">
	<Textfield
		id="routePath{i}"
		style="flex: 1;"
		label="Path"
		required
		bind:value={path}
		invalid={[requireRule(path), routeRule(path)].some(f => f)}
	/>
	<ErrorWarningTooltip
		type='warning'
		messages={[resolveIdPathParamMessage(), resolveRootPathSymbolMessage()]}
		names={['', '']}
	/>
</div>