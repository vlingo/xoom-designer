<script>
	import { requireRule } from '../validators';
	import Validation from '../util/Validation';
	import { settings } from "../stores";
	import CardForm from "../components/CardForm.svelte";
	import { Radio, TextField } from "svelte-materialify/src";
	import { deploymentTypes } from '../stores/deployment.js';

	$: valid = Validation.validateDeployment($settings);
</script>

<svelte:head>
	<title>Deployment</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="persistence" next="generation" bind:valid>
	<div class="d-flex justify-center pb-4 mb-4 mt-4">
		{#each $deploymentTypes as {label, value}}
			<Radio bind:group={$settings.deployment.type} value={value}>{label}</Radio>
		{/each}
	</div>
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