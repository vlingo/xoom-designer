<script>
	import TextField from "svelte-materialify/src/components/TextField";
	import CardForm from "../components/CardForm.svelte";
</script>

<!-- add newbie tooltips -->
<CardForm title="Context" previous="." next="model">
	<TextField placeholder="com.example">Group Id</TextField>
	<TextField placeholder="demo">Artifact Id</TextField>
	<TextField placeholder="1.0.0">Artifact Version</TextField>
	<TextField placeholder="com.example.demo">Base Package Name</TextField>
</CardForm>