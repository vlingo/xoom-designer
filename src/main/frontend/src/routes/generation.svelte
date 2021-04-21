<script>
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, aggregateSettings, persistenceSettings, deploymentSettings, generationSettings, setLocalStorage, valueObjectSettings, settingsInfo, projectGenerationIndex, generatedProjectsPaths } from "../stores";
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

  const RESPONSE_STATUS = {
    SUCCESSFUL: "SUCCESSFUL",
    FAILED: "FAILED"
  }

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
  let generateButtonLabel = requiresCompression() ? "Download Project" : "Generate";
  
  function checkPath() {
    if(requiresCompression()){
      generate();
    } else {
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
  }

  function downloadSettingsFile() {
    if(!valid) return;
    processing = true;
    dialogActive = false;
    XoomDesignerRepository.downloadSettingsFile(context, model, deployment, $generationSettings.projectDirectory, $generationSettings.useAnnotations, $generationSettings.useAutoDispatch)
    	.then(settingsFile => {
        status = RESPONSE_STATUS.SUCCESSFUL;
        success = ["Settings file generated. ", ""];
        DownloadDialog.forJsonFile("settings.json", settingsFile.encoded);
      }).catch(generationReport => {
        failure = ["Download failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"];
        status = RESPONSE_STATUS.FAILED;
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
  }

	const generate = () => {
    if(!valid) return;
    processing = true;
    dialogActive = false;
		XoomDesignerRepository.postGenerationSettings(context, model, deployment, $generationSettings.projectDirectory, $generationSettings.useAnnotations, $generationSettings.useAutoDispatch)
		  .then(generationReport => {
        if(requiresCompression()) {
          status = generationReport.status;
          success = ["Project generated. ", ""];
          DownloadDialog.forZipFile("project.zip", generationReport.compressedProject);
        } else {
          success = ["Project generated. ","Please check folder: " + $generationSettings.projectDirectory];
          status = generationReport.status;
          $projectGenerationIndex = Number($projectGenerationIndex) + 1;
          $generatedProjectsPaths = [...$generatedProjectsPaths, $generationSettings.projectDirectory];
        }
      }).catch(generationReport => {
        failure = ["Project generation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"];
        status = generationReport.status;
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

  function cancelDialog() {
    dialogActive = false
    isLoading = false;
  }

  function requiresCompression() {
    return $settingsInfo.generationTarget === "zip-download";
  }

	$: if(!$generationSettings.useAnnotations) $generationSettings.useAutoDispatch = false;
  $: valid = (requiresCompression() || $generationSettings.projectDirectory) && context && model && model.aggregateSettings && model.persistenceSettings && deployment
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
  {#if !requiresCompression()}
	<TextField class="mb-4" placeholder={$settingsInfo.userHomePath} bind:value={$generationSettings.projectDirectory} rules={[requireRule]}>Absolute path where you want to generate the project</TextField>
  {/if}
	<Switch class="mb-4" bind:checked={$generationSettings.useAnnotations}>Use VLINGO XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={$generationSettings.useAutoDispatch} disabled={!$generationSettings.useAnnotations}>Use VLINGO XOOM auto dispatch</Switch>

  <Button class="mt-4 mr-4" on:click={checkPath} disabled={!valid || processing || isLoading}>{generateButtonLabel}</Button>
  <Button class="mt-4 mr-4" on:click={downloadSettingsFile} disabled={!valid || processing || isLoading}>Download Settings File</Button>
  {#if processing}
    <ProgressCircular indeterminate color="primary" />
  {:else if status === RESPONSE_STATUS.SUCCESSFUL}
    <Icon class="green-text" path={mdiCheckBold}/> {success[0]+success[1]}
  {:else if status === RESPONSE_STATUS.FAILED}
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
