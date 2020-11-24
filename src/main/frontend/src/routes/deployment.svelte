<script>
	import Radio from "svelte-materialify/src/components/Radio";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { deploymentSettings } from "../stores";
	import { requireRule } from '../validators';

	let types = [
		{ label: "None", value: "NONE" },
		{ label: "Docker", value: "DOCKER" },
		{ label: "Kubernetes", value: "KUBERNETES" },
	]
	// [{ name: "id", type: "String" }];
	let clusterNodes = $deploymentSettings ? $deploymentSettings.clusterNodes : 3;
    let type = $deploymentSettings ? $deploymentSettings.type : "NONE";
    let dockerImage = $deploymentSettings ? $deploymentSettings.dockerImage : "";
    let kubernetesImage = $deploymentSettings ? $deploymentSettings.kubernetesImage : "";
	let kubernetesPod = $deploymentSettings ? $deploymentSettings.kubernetesPod : "";

	$: valid = clusterNodes && type && (type === "NONE" ? true : dockerImage && (type === "DOCKER" ? true : kubernetesImage && kubernetesPod));
	$: $deploymentSettings = valid ? { clusterNodes, type: type, dockerImage, kubernetesImage, kubernetesPod } : undefined;
	$: console.log($deploymentSettings);
</script>

<svelte:head>
	<title>Deployment</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation">
	{#each types as {label, value}}
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