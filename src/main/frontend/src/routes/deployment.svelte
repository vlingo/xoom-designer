<script>
	import { Radio, TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { deploymentSettings, onDeploymentSettingsChange, setLocalStorage, EDITION_STATUS } from "../stores";
	import { requireRule } from '../validators';
	import { deploymentTypes } from '../stores/deployment.js';

	// [{ name: "id", type: "String" }];
	let clusterNodes = $deploymentSettings ? $deploymentSettings.clusterNodes : 3;
    let type = $deploymentSettings ? $deploymentSettings.type : "NONE";
    let dockerImage = $deploymentSettings ? $deploymentSettings.dockerImage : "";
    let kubernetesImage = $deploymentSettings ? $deploymentSettings.kubernetesImage : "";
	let kubernetesPod = $deploymentSettings ? $deploymentSettings.kubernetesPod : "";

	$: valid = clusterNodes >= 0 && type && (type === "NONE" ? true : dockerImage && (type === "DOCKER" ? true : kubernetesImage && kubernetesPod));
	$: if(valid) {
		$deploymentSettings = { clusterNodes, type, dockerImage, kubernetesImage, kubernetesPod };
		setLocalStorage("deploymentSettings", $deploymentSettings)
	}
	$: onDeploymentSettingsChange();
</script>

<svelte:head>
	<title>Deployment</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation" bind:valid>
	<div class="d-flex justify-center pb-4 mb-4 mt-4">
		{#each $deploymentTypes as {label, value}}
			<Radio bind:group={type} value={value} on:change={onDeploymentSettingsChange}>{label}</Radio>
		{/each}
	</div>
	{#if type === "DOCKER" || type === "KUBERNETES"}
		<TextField class="mb-4 pb-4" placeholder="demo-app" bind:value={dockerImage} rules={[requireRule]} validateOnBlur={!dockerImage} on:change={onDeploymentSettingsChange}>Local Docker Image</TextField>
	{/if}
	{#if type === "KUBERNETES"}
		<TextField class="mb-4 pb-4" placeholder="demo-application" bind:value={kubernetesImage} rules={[requireRule]} validateOnBlur={!kubernetesImage} on:change={onDeploymentSettingsChange}>Published Docker Image</TextField>
		<TextField class="mb-4 pb-4" placeholder="demo-application" bind:value={kubernetesPod} rules={[requireRule]} validateOnBlur={!kubernetesPod} on:change={onDeploymentSettingsChange}>Kubernetes POD</TextField>
	{/if}
</CardForm>

<style global>
	.s-radio {
		margin: 0 1rem;
	}

	.s-radio__wrapper {
		height: 24px;
		width: 24px;
	}
	.s-radio__background::before {
		height: 16px;
		width: 16px;
	}
</style>