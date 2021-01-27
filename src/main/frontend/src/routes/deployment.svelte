<script>
	import { Radio, TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { deploymentSettings, setLocalStorage } from "../stores";
	import { requireRule } from '../validators';
	import { deploymentTypes } from '../stores/deployment.js';

	// [{ name: "id", type: "String" }];
	let clusterNodes = $deploymentSettings ? $deploymentSettings.clusterNodes : 3;
    let type = $deploymentSettings ? $deploymentSettings.type : "NONE";
    let dockerImage = $deploymentSettings ? $deploymentSettings.dockerImage : "";
    let kubernetesImage = $deploymentSettings ? $deploymentSettings.kubernetesImage : "";
	let kubernetesPod = $deploymentSettings ? $deploymentSettings.kubernetesPod : "";

	$: valid = clusterNodes && type && (type === "NONE" ? true : dockerImage && (type === "DOCKER" ? true : kubernetesImage && kubernetesPod));
	$: if(valid) {
		$deploymentSettings = { clusterNodes, type, dockerImage, kubernetesImage, kubernetesPod };
		setLocalStorage("deploymentSettings", $deploymentSettings)
	}
	$: console.log($deploymentSettings);
</script>

<svelte:head>
	<title>Deployment</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation">
	{#each $deploymentTypes as {label, value}}
		<Radio bind:group={type} value={value}>{label}</Radio>
	{/each}
	{#if type === "DOCKER" || type === "KUBERNETES"}
	<div style="height: 16px"></div>
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-app" bind:value={dockerImage} rules={[requireRule]} validateOnBlur={!dockerImage}>Local Docker Image</TextField>
	{/if}
	{#if type === "KUBERNETES"}
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-application" bind:value={kubernetesImage} rules={[requireRule]} validateOnBlur={!kubernetesImage}>Published Docker Image</TextField>
		<TextField style="min-width: 300px" class="ma-4" placeholder="demo-application" bind:value={kubernetesPod} rules={[requireRule]} validateOnBlur={!kubernetesPod}>Kubernetes POD</TextField>
	{/if}
</CardForm>