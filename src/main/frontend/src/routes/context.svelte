<script>
	import { TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import Base64 from "../util/Base64";
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
	import { contextSettings, aggregateSettings, valueObjectSettings, persistenceSettings, onPersistenceSettingsChange, isPersistenceSettingsChanged, deploymentSettings, onDeploymentSettingsChange, isDeploymentSettingsChanged, generationSettings, onGenerationSettingsChange, isGenerationSettingsChanged, setLocalStorage } from '../stores';
	import { Button, Icon, Dialog, Card, CardTitle, CardActions, CardText } from 'svelte-materialify/src';
	import { mdiAlert, mdiCheckBold, mdiCloseThick } from "@mdi/js";
	import { artifactRule, packageRule, requireRule, versionRule } from '../validators';
	import Portal from "svelte-portal/src/Portal.svelte";

	let dialogActive = false;
	let groupId, artifactId, artifactVersion, packageName;
	let files, succeded, failed, successMessage, failureMessage;

	bindContextSettings($contextSettings);

	function bindContextSettings(contextSettings) {
		if(contextSettings) {
			groupId = contextSettings ? contextSettings.groupId : "";
			artifactId = contextSettings ? contextSettings.artifactId : "";
			artifactVersion = contextSettings ? contextSettings.artifactVersion : "";
			packageName = contextSettings ? contextSettings.packageName : "";
		}
	}

	function importSettings() {
		if(isEdited()) {
			dialogActive = true;
		} else {
			openFileExplorer();
		}
	}

	function openFileExplorer() {
		closeImportDialog();
		document.getElementById("fileImport").click();
	}

	function handleUpload() {
		Base64.encode(files[0]).then(encodedFile => {
			XoomDesignerRepository.processImportFile(encodedFile)
			.then(imported => {
				applyImportedSettings(imported);
				onPersistenceSettingsChange();
				onDeploymentSettingsChange();
				onGenerationSettingsChange();
				succeed("Settings imported.");
			}).catch(generationReport => {
				fail(["Settings importation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"]);
			}).finally(() => {
				files = [];
			})
		});
	}

	function applyImportedSettings(imported) {
		bindContextSettings(imported.context);
		$aggregateSettings = imported.model ? imported.model.aggregateSettings : [];
		$valueObjectSettings = imported.model ? imported.model.valueObjectSettings : [];
		$persistenceSettings = imported.model ? imported.model.persistenceSettings : {};
		$deploymentSettings = imported.deployment;
		$generationSettings.useAnnotations = imported.useAnnotations;
		$generationSettings.useAutoDispatch = imported.useAutoDispatch;
		$generationSettings.projectDirectory = imported.projectDirectory;
	}

	function closeImportDialog() {
		dialogActive = false;
	}

	function succeed(message) {
		successMessage = message;
		succeded = true;
		failed = false;
	}

	function fail(messages) {
		failureMessage = messages;
		succeded = false;
		failed = true;
	}

	function isEdited() {
		return groupId || artifactId || artifactVersion || packageName || 
		($aggregateSettings && $aggregateSettings.length > 0) ||  
		isPersistenceSettingsChanged() || isDeploymentSettingsChanged() || isGenerationSettingsChanged();
	}

	$: files && files.length && handleUpload();
	$: valid = !packageRule(groupId) && !artifactRule(artifactId) && !versionRule(artifactVersion) && !packageRule(packageName);
	$: if(valid) {
		$contextSettings = { groupId, artifactId, artifactVersion, packageName };
		setLocalStorage("contextSettings", $contextSettings)
	}
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Context" next="aggregates" bind:valid>
	<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={groupId} rules={[requireRule, packageRule]} validateOnBlur={!groupId}>Group Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="demo" bind:value={artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!artifactId}>Artifact Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!artifactVersion}>Artifact Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={packageName} rules={[requireRule, packageRule]} validateOnBlur={!packageName}>Base Package Name</TextField>
	<input id="fileImport" type="file" hidden={true} accept="application/json" bind:files>
	<Button class="mt-4 mr-4" on:click={importSettings}>Import Settings</Button> 
	{#if succeded}
	<Icon class="green-text" path={mdiCheckBold}/> {successMessage}
	{:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
    <a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failureMessage[1]}</a>
  	{/if}
</CardForm>

<Portal target=".s-app">
	<Dialog persistent bind:active={dialogActive}>
		<Card class="pa-3">
			<div class="d-flex flex-column">
				<CardTitle class="error-text">
          			Be Careful!
        		</CardTitle>
        		<CardText>
            		This action will overwrite the fields already filled in.
        		</CardText>
				<CardActions style="margin-top: auto" class="justify-space-around">
          			<Button on:click={closeImportDialog}>Cancel</Button>
            		<Button class="primary-color" on:click={openFileExplorer}>Continue</Button>
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>