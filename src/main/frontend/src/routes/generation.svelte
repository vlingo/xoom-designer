<script>
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, modelSettings, deploymentSettings, generationSettings } from "../stores";

	let context = $contextSettings;
    let model = $modelSettings;
    let deployment  = $deploymentSettings;
    let projectDirectory = $generationSettings ? $generationSettings.projectDirectory : "";
    let useAnnotations = $generationSettings ? $generationSettings.useAnnotations : false;
	let useAutoDispatch = $generationSettings ? $generationSettings.useAutoDispatch : false;

	$: if(!useAnnotations) useAutoDispatch = false;

	$: $generationSettings = { context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch }
	$: console.log($generationSettings);
</script>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField placeholder="D:\demo-projects" bind:value={projectDirectory}>Absolute path where you want to generate the project</TextField>
	<Switch bind:checked={useAnnotations}>Use VLINGO/XOOM annotations</Switch>
	<Switch bind:checked={useAutoDispatch} disabled={!useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>
</CardForm>