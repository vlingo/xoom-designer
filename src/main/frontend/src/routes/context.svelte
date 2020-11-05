<script>
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings } from '../stores';
	import { notEmpty, validVersion } from '../validators';
	
	let groupId = $contextSettings ? $contextSettings.groupId : "";
	let artifactId = $contextSettings ? $contextSettings.artifactId : "";
	let artifactVersion = $contextSettings ? $contextSettings.artifactVersion : "";
	let packageName = $contextSettings ? $contextSettings.packageName : "";
	let xoomVersion = "1.3.4-SNAPSHOT";

	$: $contextSettings = { groupId, artifactId, artifactVersion, packageName, xoomVersion }
	$: console.log($contextSettings);
</script>

<!-- add newbie tooltips -->
<CardForm title="Context" previous="." next="aggregates">
	<TextField class="ma-4" placeholder="com.example" bind:value={groupId} rules={[notEmpty]} validateOnBlur={!groupId}>Group Id</TextField>
	<TextField class="ma-4" placeholder="demo" bind:value={artifactId} rules={[notEmpty]} validateOnBlur={!artifactId}>Artifact Id</TextField>
	<TextField class="ma-4" placeholder="1.0.0" bind:value={artifactVersion} rules={[notEmpty, validVersion]} validateOnBlur={!artifactVersion}>Artifact Version</TextField>
	<TextField class="ma-4" placeholder="com.example.demo" bind:value={packageName} rules={[notEmpty]} validateOnBlur={!packageName}>Base Package Name</TextField>
</CardForm>