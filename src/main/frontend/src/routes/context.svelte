<script>
	import Validation from '../util/Validation';
	import { settings, isValid } from '../stores';
	import { TextField } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { artifactRule, packageRule, requireRule, versionRule } from '../validators';

	$: $isValid.context = Validation.validateContext($settings);
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->

{#if $settings && $settings.context}
<CardForm title="Context" next="aggregates" bind:valid={$isValid.context}>
	<TextField class="mb-4 pb-4" placeholder="com.example" bind:value={$settings.context.groupId} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.groupId}>Group Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="demo" bind:value={$settings.context.artifactId} rules={[requireRule, artifactRule]} validateOnBlur={!$settings.context.artifactId}>Artifact Id</TextField>
	<TextField class="mb-4 pb-4" placeholder="1.0.0" bind:value={$settings.context.artifactVersion} rules={[requireRule, versionRule]} validateOnBlur={!$settings.context.artifactVersion}>Artifact Version</TextField>
	<TextField class="mb-4 pb-4" placeholder="com.example.demo" bind:value={$settings.context.packageName} rules={[requireRule, packageRule]} validateOnBlur={!$settings.context.packageName}>Base Package Name</TextField>
</CardForm>
{/if}

