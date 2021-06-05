<script>
	import { requireRule } from '../validators';
	import Validation from '../util/Validation';
	import { settings, isValid } from "../stores";
	import CardForm from "../components/CardForm.svelte";
	import { Radio, TextField, Select } from "svelte-materialify/src";
	import { deploymentTypes } from '../stores/deployment.js';

	let httpServerPort = $settings.deployment.httpServerPort? Number($settings.deployment.httpServerPort) : null;
	let producerExchangePort = $settings.deployment.producerExchangePort? Number($settings.deployment.producerExchangePort) : null;
	let clusterPort = $settings.deployment.clusterPort? Number($settings.deployment.clusterPort) : null;

	$: $isValid.deployment = Validation.validateDeployment($settings);
	const nodes = Array((49 - 3) / 2 + 1).fill().map((_, i) => i * 2 + 3);

	$: $settings.deployment.httpServerPort = Number(httpServerPort);
	$: $settings.deployment.producerExchangePort = Number(producerExchangePort);
	$: $settings.deployment.clusterPort = Number(clusterPort);
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
  {#if $settings.deployment.type === "DOCKER" || $settings.deployment.type === "KUBERNETES"}
    <TextField class="mb-4 pb-4" placeholder="docker-image-name" bind:value={$settings.deployment.dockerImage} rules={[requireRule]} validateOnBlur={!$settings.deployment.dockerImage}>Local Docker Image</TextField>
  {/if}
  {#if $settings.deployment.type === "KUBERNETES"}
    <TextField class="mb-4 pb-4" placeholder="published-docker-image-name" bind:value={$settings.deployment.kubernetesImage} rules={[requireRule]} validateOnBlur={!$settings.deployment.kubernetesImage}>Published Docker Image</TextField>
    <TextField class="mb-4 pb-4" placeholder="k8s-pod-name" bind:value={$settings.deployment.kubernetesPod} rules={[requireRule]} validateOnBlur={!$settings.deployment.kubernetesPod}>Kubernetes POD</TextField>
  {/if}
	<TextField type="number" min="0" class="mb-4 pb-4" bind:value={httpServerPort}>HTTP Server Port: unique per service on host *</TextField>
	<TextField type="number" min="0" class="mb-4 pb-4" bind:value={producerExchangePort}>Producer Exchange Port: unique per host *</TextField>
	<TextField type="number" min="0" class="mb-4 pb-4" bind:value={clusterPort}>Cluster Port: start of unique range per service with start-port thru start-port + (total-nodes * 2) *</TextField>
	<Select class="mb-4 pb-4" bind:value={$settings.deployment.clusterTotalNodes} items={nodes}>Cluster Total Nodes: selection maximum is not a platform limit; common: 3, 5, 7, or 9</Select>
    <p>*XOOM reserved ports for locally installed tools: 9019, 17171-17176, 19090, 49101-49102</p>
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