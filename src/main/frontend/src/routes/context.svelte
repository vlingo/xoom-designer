<script>
	import { TextField, Checkbox, Select } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { artifactRule, packageRule, requireRule, versionRule, projectNameRule, frameworkRule } from '../validators';
	import Validation from '../util/Validation';
	import { settings, isValid } from '../stores';

	// let groupId = $settings.context ? $settings.context.groupId : "";
	// let artifactId = $settings.context ? $settings.context.artifactId : "";
	// let artifactVersion = $settings.context ? $settings.context.artifactVersion : "";
	// let packageName = $settings.context ? $settings.context.packageName : "";

	// let projectName = $settings.context ? $settings.context.projectName : "";
	// let outputDirectory = $settings.context ? $settings.context.outputDirectory : "";
	// let solutionFile = $settings.context ? $settings.context.solutionFile : "";
	// let projectPath = $settings.context ? $settings.context.projectPath : "";
	// let isSameDirectoryActive = $settings.context ? $settings.context.isSameDirectoryActive : false;
	// let framework = $settings.context ? $settings.context.framework : false;
	// let sdk  = $settings.context ? $settings.context.sdk : '';

	// const availableSDKs = [
  //   { name: '3.1.402', value: '3.1.402' },
  //   { name: '2.1.300', value: '2.1.300' },
  // ];
	$: $isValid.context = Validation.validateContext($settings);
	// $: $isValid.contextWithPlatform = Validation.validateContextWithPlatform($settings, $settings.platformSettings.platform === 'JVM' ? { groupId, artifactId, artifactVersion, packageName } : { projectName, outputDirectory, solutionFile, projectPath, isSameDirectoryActive, framework, sdk })
	// $: $isValid.contextWithPlatform, () => $settings.context = $settings.platformSettings.platform === 'JVM' ? { groupId, artifactId, artifactVersion, packageName } : { projectName, outputDirectory, solutionFile, projectPath, isSameDirectoryActive, framework, sdk }
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<!-- <CardForm title="Context" previous="platform" next="aggregates" bind:valid={$isValid.contextWithPlatform}>
	{#if $settings.platformSettings && $settings.platformSettings.platform === '.NET'}
		<TextField class="mb-4 pb-4" bind:value={solutionFile} rules={[requireRule, projectNameRule]}>Solution Name</TextField>
		<TextField class="mb-4 pb-4" placeholder="MyCompany.MyTechnology.FirstFeature" bind:value={projectName} rules={[requireRule, projectNameRule]}>Project Name</TextField>
		<TextField class="mb-4 pb-4" bind:value={outputDirectory} rules={[requireRule]}>Solution Directory</TextField>
		<TextField class="mb-4 pb-4" bind:value={projectPath} rules={[requireRule]}>Project Path</TextField>
		<Select class="mb-4 pb-4" items={availableSDKs} bind:value={sdk}>SDK</Select>
		<TextField class="mb-4 pb-4" bind:value={framework} rules={[requireRule, frameworkRule]}>Framework</TextField>
		<Checkbox bind:checked={isSameDirectoryActive}>Put solution and project in the same directory</Checkbox>
	{:else if $settings.platformSettings && $settings.platformSettings.platform === 'JVM'}
	<CardForm title="Context" next="aggregates" bind:valid={$isValid.context}>
		<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={$settings.context.groupId} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.groupId}>Group Id</TextField>
		<TextField class="mb-4 pb-4" placeholder="demo" bind:value={$settings.context.artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!$settings.context.artifactId}>Artifact Id</TextField>
		<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={$settings.context.artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!$settings.context.artifactVersion}>Artifact Version</TextField>
		<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={$settings.context.packageName} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.packageName}>Base Package Name</TextField>
	</CardForm>
	{/if}
</CardForm> -->

<CardForm title="Context" next="aggregates" bind:valid={$isValid.context}>
	<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={$settings.context.groupId} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.groupId}>Group Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="demo" bind:value={$settings.context.artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!$settings.context.artifactId}>Artifact Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={$settings.context.artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!$settings.context.artifactVersion}>Artifact Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={$settings.context.packageName} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.packageName}>Base Package Name</TextField>
</CardForm>
