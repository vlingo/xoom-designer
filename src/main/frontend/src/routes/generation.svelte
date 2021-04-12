<script>
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, setLocalStorage, valueObjectSettings, settingsInfo, projectGenerationIndex, generatedProjectsPaths } from "../stores";
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
	import { requireRule } from "../validators";
  import { mdiCheckBold, mdiCloseThick } from "@mdi/js";
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
  let isLoading = false;

  function checkPath() {
    isLoading = true;
    Repository.post('/generation-paths', {
      path: $generationSettings.projectDirectory
    })
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
        isLoading = false;
      })
  }

	const generate = () => {
    if(!valid) return;
    processing = true;
    dialogActive = false;
		XoomDesignerRepository.postGenerationSettings(context, model, deployment, $generationSettings)
		  .then(s => {
        success = ["Project generated. ","Please check folder: " + $generationSettings.projectDirectory];
        status = s;
        $projectGenerationIndex = Number($projectGenerationIndex) + 1;
        $generatedProjectsPaths = [...$generatedProjectsPaths, $generationSettings.projectDirectory];
      }).catch(e => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"];
        status = e;
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

  function cancelDialog() {
    dialogActive = false
    isLoading = false;
  }

	$: if(!$generationSettings.useAnnotations) $generationSettings.useAutoDispatch = false;
  $: valid = $generationSettings.projectDirectory && context && model && model.aggregateSettings && model.persistenceSettings && deployment

  let value = $generationSettings.generationTarget && $generationSettings.generationTarget === 'zip-download';
  $: $generationSettings.generationTarget = value ? 'zip-download' : 'filesystem';
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
  <div class="vl-switch d-flex justify-center mb-8 mt-8">
    <b>Filesystem</b>
    <Switch class="mr-4 ml-8" bind:checked={value} inset></Switch>
    <b>Zip Download</b>
  </div>

  {#if $generationSettings.generationTarget === 'filesystem'}
    <TextField class="mb-4" placeholder={$settingsInfo.userHomePath} bind:value={$generationSettings.projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
  {:else if $generationSettings.generationTarget === 'zip-download'}
    <div class="mb-6" style="color: var(--theme-text-primary); font-size: 14px;">
      <b>Note:</b> When ready click Generate and your project will download as a zip file.
    </div>
  {/if}
  <Switch class="mb-4" bind:checked={$generationSettings.useAnnotations}>Use VLINGO/XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={$generationSettings.useAutoDispatch} disabled={!$generationSettings.useAnnotations}>Use VLINGO/XOOM auto dispatch</Switch>

  <Button class="mt-4 mr-4" on:click={checkPath} disabled={!valid || processing || isLoading}>Generate</Button>
  {#if processing}
    <ProgressCircular indeterminate color="primary" />
  {:else if status === "SUCCESSFUL"}
    <Icon class="green-text" path={mdiCheckBold}/> {success[0]+success[1]}
  {:else if status === "FAILED"}
    <Icon class="red-text" path={mdiCloseThick}/> {failure[0]}
    <a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failure[1]}</a>
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
          <Button on:click={cancelDialog}>{dialogStatus === 'Forbidden' ? 'OK' : 'Cancel'}</Button>
          {#if dialogStatus === 'Conflict'}
            <Button class="primary-color" disabled={!valid || processing} on:click={generate}>Generate</Button>
          {/if}
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>

<style global lang="scss">
	.vl-switch {
		.s-switch, .s-switch__wrapper.inset, .s-switch__wrapper.inset .s-switch__track {
			width: 100px;
		}
		.s-switch__wrapper>input:checked~.s-switch__thumb {
			transform: translate(72px);
		}
	}
</style> 