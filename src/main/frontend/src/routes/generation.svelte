<script>
	import Button from "svelte-materialify/src/components/Button";
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings } from "../stores";
	import XoomStarterRepository from "../api/XoomStarterRepository";
	import { requireRule } from "../validators";

	let context = $contextSettings;
    let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings };
    let deployment  = $deploymentSettings;
    let projectDirectory = $generationSettings ? $generationSettings.projectDirectory : "";
    let useAnnotations = $generationSettings ? $generationSettings.useAnnotations : false;
	let useAutoDispatch = $generationSettings ? $generationSettings.useAutoDispatch : false;

	const generate = () => {
		if(!projectDirectory) return;
		XoomStarterRepository.postGenerationSettings(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch)
		.then(console.log);
	}
	$: if(!useAnnotations) useAutoDispatch = false;

	$: console.log(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch);
</script>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField style="min-width: 400px" placeholder="D:\demo-projects" bind:value={projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
	<Switch bind:checked={useAnnotations}>Use VLINGO/XOOM annotations</Switch>
	<Switch bind:checked={useAutoDispatch} disabled={!useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>
	<Button on:click={generate} disabled={!projectDirectory}>Generate</Button>
</CardForm>