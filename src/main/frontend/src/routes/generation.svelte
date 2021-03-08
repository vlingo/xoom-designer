<script>
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, setLocalStorage, valueObjectSettings } from "../stores";
	import XoomStarterRepository from "../api/XoomStarterRepository";
	import { requireRule } from "../validators";
  import { mdiCheckBold, mdiCloseThick } from "@mdi/js";
  import {
    Button,
    Switch,
    TextField,
    ProgressCircular,
    Snackbar,
    Icon
  } from 'svelte-materialify/src';

	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings, valueObjectSettings: $valueObjectSettings };
  let deployment  = $deploymentSettings;
  let projectDirectory = $generationSettings ? $generationSettings.projectDirectory : "";
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
        $valueObjectSettings = [];
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
	<TextField class="mb-4" placeholder="D:\demo-projects" bind:value={projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
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
