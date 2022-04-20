<script>
	import { TextField, Checkbox, Select } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { artifactRule, packageRule, requireRule, versionRule, projectNameRule, namespaceRule } from '../validators';
	import Validation from '../util/Validation';
	import { settings, isValid } from '../stores';

	$: isJvmPlatform = !$settings.platformSettings || $settings.platformSettings.platform === 'JVM';
	$: isDotNetPlatform = $settings.platformSettings && $settings.platformSettings.platform === '.NET';
	$: $isValid.jvmContext = Validation.validateContext($settings);
	$: $isValid.dotNetContext = Validation.validatePlatformWithContext($settings, $settings.context);
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

{#if isJvmPlatform}
<CardForm title="Context" next="aggregates" bind:valid={$isValid.jvmContext}>
	<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={$settings.context.groupId} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.groupId}>Group Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="demo" bind:value={$settings.context.artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!$settings.context.artifactId}>Artifact Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={$settings.context.artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!$settings.context.artifactVersion}>Artifact Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={$settings.context.packageName} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.packageName}>Base Package Name</TextField>
</CardForm>
{:else if isDotNetPlatform}
<CardForm title="Context" next="aggregates" bind:valid={$isValid.dotNetContext}>
	<TextField class="mb-4 pb-4" placeholder="Example" bind:value={$settings.context.solutionName} rules={[requireRule, projectNameRule]} validateOnBlur={!$settings.context.solutionName}>Solution Name</TextField>
	<TextField class="mb-4 pb-4" placeholder="Demo" bind:value={$settings.context.projectName} rules={[requireRule, projectNameRule]} validateOnBlur={!$settings.context.projectName}>Project Name</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={$settings.context.projectVersion} rules={[requireRule, versionRule]} validateOnBlur={!$settings.context.projectVersion}>Project Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="Io.Example.Demo " bind:value={$settings.context.namespace} rules={[requireRule, namespaceRule]} validateOnBlur={!$settings.context.namespace}>Base Namespace</TextField>
</CardForm>
{/if}
