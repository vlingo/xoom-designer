<script>
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";

	let annotations = false;
	let autoDispatch;
	$: if(!annotations) autoDispatch = false;
</script>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField placeholder="D:\demo-projects">Absolute path where you want to generate the project</TextField>
	<Switch bind:checked={annotations}>Use VLINGO/XOOM annotations</Switch>
	<Switch bind:checked={autoDispatch} disabled={!annotations}>Use VLINGO/XOOM auto dispatch</Switch>
</CardForm>