<script>
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, setLocalStorage, valueObjectSettings, settingsInfo, projectGenerationIndex, generatedProjectsPaths } from "../stores";
	import XoomStarterRepository from "../api/XoomStarterRepository";
	import { requireRule } from "../validators";
  import { mdiAlert, mdiCheckBold, mdiCloseThick } from "@mdi/js";
  import {
    Button,
    Switch,
    TextField,
    ProgressCircular,
    Snackbar,
    Icon,
    Dialog,
    Card,
    CardTitle,
    CardActions,
    CardText,
    Alert
  } from 'svelte-materialify/src';
  import Portal from "svelte-portal/src/Portal.svelte";


	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings, valueObjectSettings: $valueObjectSettings };
  let deployment  = $deploymentSettings;
  let projectDirectory = $generationSettings ? $generationSettings.projectDirectory : $contextSettings ? `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}${$contextSettings.groupId}${$settingsInfo.pathSeparator}${$contextSettings.artifactId}${$projectGenerationIndex}` : `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}`;
  let useAnnotations = $generationSettings ? $generationSettings.useAnnotations : false;
  let useAutoDispatch = $generationSettings ? $generationSettings.useAutoDispatch : false;
  let processing = false;
  let status;
  let snackbar = false;
  let success;
  let failure;
  let dialogActive = false;

	const generate = () => {
    if(!valid) return;
    processing = true;
		XoomStarterRepository.postGenerationSettings(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch)
		  .then(s => {
        success = ["Project generated. ","Please check folder: " + projectDirectory];
        status = s;
        const tempProjectGenerationIndex = $projectGenerationIndex + 1;
        const tempGeneratedProjectsPaths = [...$generatedProjectsPaths, projectDirectory];
        localStorage.clear();
        $projectGenerationIndex = tempProjectGenerationIndex;
        $generatedProjectsPaths = tempGeneratedProjectsPaths;
      }).catch(e => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/vlingo-xoom-starter/issues"];
        status = e;
      }).finally(() => {
        processing = false;
        snackbar = true;
        dialogActive = false;
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

  <Button class="mt-4 mr-4" on:click={() => dialogActive = true} disabled={!valid}>Generate</Button>
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

<Portal target=".s-app">
	<Dialog persistent bind:active={dialogActive}>
		<Card class="pa-3">
			<div class="d-flex flex-column">
				<CardTitle class="error-text">
          Be Careful!
        </CardTitle>
        {#if $generatedProjectsPaths.includes(projectDirectory)}
          <CardText>
            You already generated a project with the same path. If that project still exists and you continue, that project will be overwritten.
          </CardText>
        {:else}
          <CardText>
            Warning: You are about to potentially overwrite a previously generated project. Are you sure?
          </CardText>
        {/if}
				<CardActions style="margin-top: auto" class="justify-space-around">
          <Button on:click={() => dialogActive = false}>Cancel</Button>
          <Button class="primary-color" disabled={!valid} on:click={generate}>Generate</Button>
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>
