<script>
	import Button from "svelte-materialify/src/components/Button";
	import Switch from "svelte-materialify/src/components/Switch";
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings } from "../stores";
	import XoomStarterRepository from "../api/XoomStarterRepository";
	import { requireRule } from "../validators";
  import ProgressCircular from "svelte-materialify/src/components/ProgressCircular/ProgressCircular.svelte";
  import Snackbar from "svelte-materialify/src/components/Snackbar/Snackbar.svelte"
  import Icon from "svelte-materialify/src/components/Icon";
  import { mdiCheckBold, mdiCloseThick } from "@mdi/js";

	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings };
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
    if(invalid) return;
    processing = true;
		XoomStarterRepository.postGenerationSettings(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch)
		  .then(s => {
        success = ["Project generated. ","Please check folder: " + projectDirectory + "\\" + context.artifactId];
        status = s;
        console.log(status);
        processing = false;
        snackbar = true;
      }).catch(e => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/vlingo-xoom-starter/issues"];
        status = e;
        console.log(status);
        processing = false;
        snackbar = true;
      })
	}

	$: if(!useAnnotations) useAutoDispatch = false;
	$: invalid = !projectDirectory || !context || !model || !model.aggregateSettings || !model.persistenceSettings || !deployment
	$: $generationSettings = { projectDirectory, useAnnotations, useAutoDispatch }
	$: console.log(context, model, deployment, $generationSettings);
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField placeholder="D:\demo-projects" bind:value={projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
	<Switch bind:checked={useAnnotations}>Use VLINGO/XOOM annotations</Switch>
  <Switch bind:checked={useAutoDispatch} disabled={!useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>

  <Button class="mr-3" on:click={generate} disabled={invalid}>Generate</Button>
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
