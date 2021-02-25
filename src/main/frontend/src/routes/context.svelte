<script>
	import { TextField, Checkbox, Select } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { contextSettings, platformSettings, setLocalStorage } from '../stores';
	import { artifactRule, packageRule, requireRule, versionRule, xoomVersionRule, projectNameRule, frameworkRule } from '../validators';
	
	let groupId = $contextSettings ? $contextSettings.groupId : "";
	let artifactId = $contextSettings ? $contextSettings.artifactId : "";
	let artifactVersion = $contextSettings ? $contextSettings.artifactVersion : "";
	let packageName = $contextSettings ? $contextSettings.packageName : "";
	let xoomVersion = "1.4.1-SNAPSHOT";

	let projectName = $contextSettings ? $contextSettings.projectName : "";
	let outputDirectory = $contextSettings ? $contextSettings.outputDirectory : "";
	let solutionFile = $contextSettings ? $contextSettings.solutionFile : "";
	let projectPath = $contextSettings ? $contextSettings.projectPath : "";
	let isSameDirectoryActive = $contextSettings ? $contextSettings.isSameDirectoryActive : false;
	let framework = $contextSettings ? $contextSettings.framework : false;
	let sdk  = $contextSettings ? $contextSettings.sdk : '';

	const availableSDKs = [
    { name: '3.1.402', value: '3.1.402' },
    { name: '2.1.300', value: '2.1.300' },
  ];
	$: valid = !!$platformSettings && (($platformSettings.platform === 'JVM' && !packageRule(groupId) && !artifactRule(artifactId) && !versionRule(artifactVersion) && !packageRule(packageName) && !xoomVersionRule(xoomVersion)) || ($platformSettings.platform === '.NET' && projectName && !!outputDirectory && !!solutionFile && !!projectPath && !!framework && !!sdk && !projectNameRule(solutionFile) && !projectNameRule(projectName) && !frameworkRule(framework)));
	$: if(valid) {
		$contextSettings = $platformSettings.platform === 'JVM' ? { groupId, artifactId, artifactVersion, packageName, xoomVersion } : { projectName, outputDirectory, solutionFile, projectPath, isSameDirectoryActive, framework, sdk };
		setLocalStorage("contextSettings", $contextSettings)
	}
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Context" previous="platform" next="aggregates" bind:valid>
	{#if $platformSettings && $platformSettings.platform === '.NET'}
		<TextField class="mb-4 pb-4" bind:value={solutionFile} rules={[requireRule, projectNameRule]}>Solution Name</TextField>
		<TextField class="mb-4 pb-4" placeholder="MyCompany.MyTechnology.FirstFeature" bind:value={projectName} rules={[requireRule, projectNameRule]}>Project Name</TextField>
		<TextField class="mb-4 pb-4" bind:value={outputDirectory} rules={[requireRule]}>Solution Directory</TextField>
		<TextField class="mb-4 pb-4" bind:value={projectPath} rules={[requireRule]}>Project Path</TextField>
		<Select class="mb-4 pb-4" items={availableSDKs} bind:value={sdk}>SDK</Select>
		<TextField class="mb-4 pb-4" bind:value={framework} rules={[requireRule, frameworkRule]}>Framework</TextField>
		<Checkbox bind:checked={isSameDirectoryActive}>Put solution and project in the same directory</Checkbox>
	{:else if $platformSettings && $platformSettings.platform === 'JVM'}
		<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={groupId} rules={[requireRule, packageRule]} validateOnBlur={!groupId}>Group Id</TextField>
		<TextField class="mb-4 pb-4" placeholder="demo" bind:value={artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!artifactId}>Artifact Id</TextField>
		<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!artifactVersion}>Artifact Version</TextField>
		<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={packageName} rules={[requireRule, packageRule]} validateOnBlur={!packageName}>Base Package Name</TextField>
	{/if}
</CardForm>