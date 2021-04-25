<script>
	import { TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { importedSettings, contextSettings, setLocalStorage, onContextSettingsChange } from '../stores';
	import { artifactRule, packageRule, requireRule, versionRule } from '../validators';

	let dialogActive = false;
	let groupId, artifactId, artifactVersion, packageName;
	let files, succeded, failed, successMessage, failureMessage;
	
	loadSettings($contextSettings);

	function loadSettings(context) {
		if(context) {
			groupId = context.groupId;
			artifactId = context.artifactId;
			artifactVersion = context.artifactVersion;
			packageName = context.packageName;
		} else {
			clear();
		}
	}

    function importSettings() {
		loadSettings($importedSettings.context);
		save();
	}

	function clear() {
		groupId = "";
		artifactId = "";
		artifactVersion = "";
		packageName = "";
	}

	function save() {
		$contextSettings = { groupId, artifactId, artifactVersion, packageName };
		setLocalStorage("contextSettings", $contextSettings);
		onContextSettingsChange();
	}

	$: $importedSettings && importSettings()
	$: valid = !packageRule(groupId) && !artifactRule(artifactId) && !versionRule(artifactVersion) && !packageRule(packageName);
	$: groupId , artifactId , artifactVersion , packageName , save();
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
</CardForm>
