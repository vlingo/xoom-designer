<script>
	import Radio from "svelte-materialify/src/components/Radio";
import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";

	let group = "None";
	let radios = [
		{ label: "None" },
		{ label: "Docker" },
		{ label: "Kubernetes" },
	]
	$: console.log(group);
</script>

<!-- add newbie tooltips -->
<CardForm title="Deployment" previous="model" next="generation">
	{#each radios as radio}
		<Radio bind:group value={radio.label}>{radio.label}</Radio>
	{/each}
	{#if group === "Docker" || group === "Kubernetes"}
		<TextField placeholder="demo-app">Local Docker Image</TextField>
	{/if}
	{#if group === "Kubernetes"}
		<TextField placeholder="demo-application">Published Docker Image</TextField>
		<TextField placeholder="demo-application">Kubernetes POD</TextField>
	{/if}
</CardForm>