<script>
	import Radio from "svelte-materialify/src/components/Radio";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { deploymentSettings } from "../stores";
	import { requireRule } from '../validators';

	let types = [
		{ label: "None" },
		{ label: "Docker" },
		{ label: "Kubernetes" },
	]

	let clusterNodes = $deploymentSettings ? $deploymentSettings.clusterNodes : 3;
    let type = $deploymentSettings ? $deploymentSettings.type : "None";
    let dockerImage = $deploymentSettings ? $deploymentSettings.dockerImage : "";
    let kubernetesImage = $deploymentSettings ? $deploymentSettings.kubernetesImage : "";
	let kubernetesPod = $deploymentSettings ? $deploymentSettings.kubernetesPod : "";

	$: $deploymentSettings = { clusterNodes, type: type.toUpperCase(), dockerImage, kubernetesImage, kubernetesPod }
	$: console.log($deploymentSettings);
</script>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation">
	{#each types as {label}}
		<Radio bind:group={type} value={label}>{label}</Radio>
	{/each}
	{#if type === "Docker" || type === "Kubernetes"}
	<div style="height: 16px"></div>
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-app" bind:value={dockerImage} rules={[requireRule]} validateOnBlur={!dockerImage}>Local Docker Image</TextField>
	{/if}
	{#if type === "Kubernetes"}
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-application" bind:value={kubernetesImage} rules={[requireRule]} validateOnBlur={!kubernetesImage}>Published Docker Image</TextField>
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-application" bind:value={kubernetesPod} rules={[requireRule]} validateOnBlur={!kubernetesPod}>Kubernetes POD</TextField>
	{/if}
</CardForm>