<script>
	import { TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import Base64 from "../util/Base64";
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
	import { contextSettings, setLocalStorage } from '../stores';
	import { Button, Icon } from 'svelte-materialify/src';
	import { mdiAlert, mdiCheckBold, mdiCloseThick } from "@mdi/js";
	import { artifactRule, packageRule, requireRule, versionRule, xoomVersionRule } from '../validators';

	let files;
	let succeded, failed, success, failure;
	let groupId = $contextSettings ? $contextSettings.groupId : "";
	let artifactId = $contextSettings ? $contextSettings.artifactId : "";
	let artifactVersion = $contextSettings ? $contextSettings.artifactVersion : "";
	let packageName = $contextSettings ? $contextSettings.packageName : "";
	let xoomVersion = "1.4.1-SNAPSHOT";

	function uploadSettings() {
		document.getElementById("fileUpload").click();
	}

	function handleUpload() {
		Base64.encode(files[0]).then(encodedFile => {
			XoomDesignerRepository.uploadSettingsFile(encodedFile)
			.then(settingsData => {
				succeded = true;
				failed = false;
				success = "Settings file uploaded.";
			}).catch(generationReport => {
				succeded = false;
				failed = true;
				failure = ["Upload failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"];
			}).finally(() => {
				files = [];
			})
		});
	}

	$: files && files.length && handleUpload();
	$: valid = !packageRule(groupId) && !artifactRule(artifactId) && !versionRule(artifactVersion) && !packageRule(packageName) && !xoomVersionRule(xoomVersion);
	$: if(valid) {
		$contextSettings = { groupId, artifactId, artifactVersion, packageName, xoomVersion };
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
	<input id="fileUpload" type="file" hidden={true} accept="application/json" bind:files>
	<Button class="mt-4 mr-4" on:click={uploadSettings}>Upload Settings</Button> 
	{#if succeded}
	<Icon class="green-text" path={mdiCheckBold}/> {success}
	{:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failure[0]}
    <a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failure[1]}</a>
  	{/if}
</CardForm>
