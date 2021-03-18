<script>
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, setLocalStorage, valueObjectSettings, settingsInfo } from "../stores";
	import XoomStarterRepository from "../api/XoomStarterRepository";
	import { requireRule } from "../validators";
  import { mdiAlert, mdiCheckBold, mdiCloseThick } from "@mdi/js";
  import {
    Button,
    Switch,
    TextField,
    ProgressCircular,
    Snackbar,
    Icon
  } from 'svelte-materialify/src';
  import Alert from "svelte-materialify/src/components/Alert";

	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings, valueObjectSettings: $valueObjectSettings };
  let deployment  = $deploymentSettings;
  let projectDirectory = $generationSettings ? $generationSettings.projectDirectory : $contextSettings ? `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}${$contextSettings.groupId}${$settingsInfo.pathSeparator}${$contextSettings.artifactId}${$settingsInfo.pathSeparator}projectGenerationIndex` : `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}`;
  let useAnnotations = $generationSettings ? $generationSettings.useAnnotations : false;
  let useAutoDispatch = $generationSettings ? $generationSettings.useAutoDispatch : false;
  let processing = false;
  let status;
  let snackbar = false;
  let success;
  let failure;

	const generate = () => {
    if(!valid) return;
    processing = true;
		XoomStarterRepository.postGenerationSettings(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch)
		  .then(s => {
        success = ["Project generated. ","Please check folder: " + projectDirectory + "\\" + context.artifactId];
        status = s;
      }).catch(e => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/vlingo-xoom-starter/issues"];
        status = e;
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

	$: if(!useAnnotations) useAutoDispatch = false;
  $: valid = projectDirectory && context && model && model.aggregateSettings && model.persistenceSettings && deployment
  $: if(projectDirectory) {
    $generationSettings = { projectDirectory, useAnnotations, useAutoDispatch }
    setLocalStorage("generationSettings", $generationSettings)
	}
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField class="mb-4" placeholder={$settingsInfo.userHomePath} bind:value={projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
	<Switch class="mb-4" bind:checked={useAnnotations}>Use VLINGO/XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={useAutoDispatch} disabled={!useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>

  <Button class="mt-4 mr-4" on:click={generate} disabled={!valid}>Generate</Button>
  {#if processing}
    <ProgressCircular indeterminate color="primary" />
  {:else if status === "SUCCESSFUL"}
    <Icon class="green-text" path={mdiCheckBold}/> {success[0]+success[1]}
  {:else if status === "FAILED"}
    <Icon class="red-text" path={mdiCloseThick}/> {failure[0]}
    <a href="https://github.com/vlingo/vlingo-xoom-starter/issues" rel="noopener" target="_blank">{failure[1]}</a>
  {/if}
  <Alert class="info-color mt-5">
    <div slot="icon">
      <Icon path={mdiAlert} />
    </div>
    <p class="mb-1">Project generation depends on Maven and requires the use of mvnw. Ensure that Maven is installed and that mvnw has file execution permission for your user account.</p>
    <p class="mb-1">For example: <code>chmod mvnw 755</code></p>
  </Alert>
</CardForm>


<Snackbar class="justify-space-between" bind:active={snackbar} top right>
  {#if status === "SUCCESSFUL"}
    <Icon class="green-text" path={mdiCheckBold}/> {success[0]}
  {:else if status === "FAILED"}
    <Icon class="red-text" path={mdiCloseThick}/> {failure[0]}
  {/if}
  <Button text on:click={() => snackbar = false}>
    Dismiss
  </Button>
</Snackbar>
