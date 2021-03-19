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
	import Repository from '../api/Repository';


	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings, valueObjectSettings: $valueObjectSettings };
  let deployment  = $deploymentSettings;
  $: $generationSettings.projectDirectory = $contextSettings ? `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}${$contextSettings.groupId}${$settingsInfo.pathSeparator}${$contextSettings.artifactId}${Number($projectGenerationIndex)}` : `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}`;
  let processing = false;
  let status;
  let snackbar = false;
  let success;
  let failure;
  let dialogActive = false;
  let dialogStatus;

  function checkPath() {
    processing = true;
    Repository.post('/generation-paths', $generationSettings.projectDirectory)
			.then(response => {
        if(response.status === 201) {
          generate();
        } else if (response.status === 403) {
          dialogStatus = 'Forbidden';
          dialogActive = true;
        } else if (response.status === 409) {
          dialogStatus = 'Conflict';
          dialogActive = true;
        }
      })
      .finally(() => {
        processing = false;
      })
  }

	const generate = () => {
    if(!valid) return;
    processing = true;
    dialogActive = false;
		XoomStarterRepository.postGenerationSettings(context, model, deployment, $generationSettings.projectDirectory, $generationSettings.useAnnotations, $generationSettings.useAutoDispatch)
		  .then(s => {
        success = ["Project generated. ","Please check folder: " + $generationSettings.projectDirectory];
        status = s;
        $projectGenerationIndex = Number($projectGenerationIndex) + 1;
        $generatedProjectsPaths = [...$generatedProjectsPaths, $generationSettings.projectDirectory];
      }).catch(e => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/vlingo-xoom-starter/issues"];
        status = e;
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

	$: if(!$generationSettings.useAnnotations) $generationSettings.useAutoDispatch = false;
  $: valid = $generationSettings.projectDirectory && context && model && model.aggregateSettings && model.persistenceSettings && deployment
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
	<TextField class="mb-4" placeholder={$settingsInfo.userHomePath} bind:value={$generationSettings.projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
	<Switch class="mb-4" bind:checked={$generationSettings.useAnnotations}>Use VLINGO/XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={$generationSettings.useAutoDispatch} disabled={!$generationSettings.useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>

  <Button class="mt-4 mr-4" on:click={checkPath} disabled={!valid || processing}>Generate</Button>
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
        {#if dialogStatus === 'Conflict'}
          <CardText>
            You already generated a project with the same path. If that project still exists and you continue, that project will be overwritten.
          </CardText>
        {:else if dialogStatus === 'Forbidden'}
          <CardText>
            Warning: the directory cannot be used for generation!
          </CardText>
        {/if}
				<CardActions style="margin-top: auto" class="justify-space-around">
          <Button on:click={() => dialogActive = false}>{dialogStatus === 'Forbidden' ? 'OK' : 'Cancel'}</Button>
          {#if dialogStatus === 'Conflict'}
            <Button class="primary-color" disabled={!valid || processing} on:click={generate}>Generate</Button>
          {/if}
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>
