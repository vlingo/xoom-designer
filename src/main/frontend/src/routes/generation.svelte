<script>
	import CardForm from "../components/CardForm.svelte";
	import { importedSettings, contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, onGenerationSettingsChange, setLocalStorage, valueObjectSettings, settingsInfo, projectGenerationIndex, generatedProjectsPaths} from "../stores";
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
  import DownloadDialog from "../util/DownloadDialog";
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
  } from 'svelte-materialify/src';
  import Portal from "svelte-portal/src/Portal.svelte";
	import Repository from '../api/Repository';

	let context = $contextSettings;
  let model = { aggregateSettings: $aggregateSettings, persistenceSettings: $persistenceSettings, valueObjectSettings: $valueObjectSettings };
  let deployment  = $deploymentSettings;
  let processing = false;
  let snackbar = false;
  let dialogActive = false;
  let isLoading = false;
  let generateButtonLabel = requiresCompression() ? "Download Project" : "Generate";
  let dialogStatus, succeded, failed, successMessage, failureMessage;
  $: $generationSettings.projectDirectory = $contextSettings ? `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}${$contextSettings.groupId}${$settingsInfo.pathSeparator}${$contextSettings.artifactId}${Number($projectGenerationIndex)}` : `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}`;
  
  function importSettings() {
    onGenerationSettingsChange()
    $generationSettings.useAnnotations = $importedSettings.useAnnotations == undefined ? true : $importedSettings.useAnnotations;
    $generationSettings.useAutoDispatch = $importedSettings.useAutoDispatch == undefined ? true : $importedSettings.useAnnotations;
    $generationSettings.projectDirectory = $importedSettings.projectDirectory == undefined ? $generationSettings.projectDirectory : $importedSettings.projectDirectory;		 
		setLocalStorage("generationSetings", $generationSettings);
	}

  function checkPath() {
    if(requiresCompression()){
      generate();
    } else {
      isLoading = true;
      XoomDesignerRepository.checkPath($generationSettings.projectDirectory)
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
  }

	const generate = () => {
    if(!valid) return;
    processing = true;
    dialogActive = false;
		XoomDesignerRepository.generateProject(context, model, deployment, $generationSettings.projectDirectory, $generationSettings.useAnnotations, $generationSettings.useAutoDispatch)
		  .then(generationReport => {
        if(requiresCompression()) {
          succeed(["Project generated. ", ""]);
          DownloadDialog.forZipFile("project.zip", generationReport.compressedProject);
        } else {
          succeed(["Project generated. ","Please check folder: " + $generationSettings.projectDirectory]);
          $projectGenerationIndex = Number($projectGenerationIndex) + 1;
          $generatedProjectsPaths = [...$generatedProjectsPaths, $generationSettings.projectDirectory];
        }        
      }).catch(generationReport => {
        fail(["Project generation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"]);
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

  function cancelDialog() {
    dialogActive = false
    isLoading = false;
  }

  function succeed(messages) {
    successMessage = messages;
    succeded = true;
    failed = false;
  }

  function fail(messages) {
    failureMessage = messages;
    succeded = false;
    failed = true;
  }

  function requiresCompression() {
    return $settingsInfo.generationTarget === "zip-download";
  }

  function validatePreviousSteps() {
    let context = $contextSettings;
    return context && context.groupId && context.artifactId &&  context.packageName && 
           context.artifactVersion && $aggregateSettings && $aggregateSettings.length > 0 && 
           $persistenceSettings && $deploymentSettings;
  }

  $: $importedSettings && importSettings()
	$: if(!$generationSettings.useAnnotations) $generationSettings.useAutoDispatch = false;
  $: valid = (requiresCompression() || $generationSettings.projectDirectory) && validatePreviousSteps();
  $: onGenerationSettingsChange();		
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
  {#if !requiresCompression()}
	<TextField class="mb-4" placeholder={$settingsInfo.userHomePath} bind:value={$generationSettings.projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
  {/if}
	<Switch class="mb-4" bind:checked={$generationSettings.useAnnotations} on:change={onGenerationSettingsChange}>Use VLINGO XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={$generationSettings.useAutoDispatch} disabled={!$generationSettings.useAnnotations} on:change={onGenerationSettingsChange}>Use VLINGO XOOM auto dispatch</Switch>
  
  <Button class="mt-4 mr-4" on:click={checkPath} disabled={!valid || processing || isLoading}>{generateButtonLabel}</Button>
  {#if processing}
    <ProgressCircular indeterminate color="primary" />
  {:else if succeded}
    <Icon class="green-text" path={mdiCheckBold}/> {successMessage[0]+successMessage[1]}
  {:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
    <a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failureMessage[1]}</a>
  {/if}
</CardForm>


<Snackbar class="justify-space-between" bind:active={snackbar} top right>
  {#if succeded}
    <Icon class="green-text" path={mdiCheckBold}/> {successMessage[0]}
  {:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
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
