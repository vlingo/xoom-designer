<script>
	import { requireRule } from '../validators';
	import Validation from '../util/Validation';
	import { settings, isValid } from "../stores";
	import CardForm from "../components/CardForm.svelte";
	import { Radio, TextField, Select } from "svelte-materialify/src";
	import { deploymentTypes } from '../stores/deployment.js';

	$: $isValid.deployment = Validation.validateDeployment($settings);
	const nodes = Array((49 - 3) / 2 + 1).fill().map((_, i) => i * 2 + 3);
</script>

<svelte:head>
	<title>Deployment</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation" bind:valid={$isValid.deployment}>
	<div class="d-flex justify-center pb-4 mb-4 mt-4">
		{#each $deploymentTypes as {label, value}}
			<Radio bind:group={$settings.deployment.type} value={value}>{label}</Radio>
		{/each}
	</div>
	<TextField class="mb-4 pb-4" bind:value={$settings.deployment.httpServerPort}>HTTP Server Port</TextField>
	<TextField class="mb-4 pb-4" bind:value={$settings.deployment.producerExchangePort}>Producer Message Exchange Port</TextField>
	<TextField class="mb-4 pb-4" bind:value={$settings.deployment.clusterPort}>Cluster Port</TextField>
	<Select class="mb-4 pb-4" bind:value={$settings.deployment.clusterTotalNodes} items={nodes}>Cluster Total Nodes</Select>
	{#if $settings.deployment.type === "DOCKER" || $settings.deployment.type === "KUBERNETES"}
		<TextField class="mb-4 pb-4" placeholder="demo-app" bind:value={$settings.deployment.dockerImage} rules={[requireRule]} validateOnBlur={!$settings.deployment.dockerImage}>Local Docker Image</TextField>
	{/if}
	{#if $settings.deployment.type === "KUBERNETES"}
		<TextField class="mb-4 pb-4" placeholder="demo-application" bind:value={$settings.deployment.kubernetesImage} rules={[requireRule]} validateOnBlur={!$settings.deployment.kubernetesImage}>Published Docker Image</TextField>
		<TextField class="mb-4 pb-4" placeholder="demo-application" bind:value={$settings.deployment.kubernetesPod} rules={[requireRule]} validateOnBlur={!$settings.deployment.kubernetesPod}>Kubernetes POD</TextField>
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