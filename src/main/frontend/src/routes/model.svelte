<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	import Select from "svelte-materialify/src/components/Select";
	import Switch from "svelte-materialify/src/components/Switch";
	import CardForm from "../components/CardForm.svelte";
</script>

<!-- add newbie tooltips -->
<CardForm title="Model" previous="context" next="deployment">
	<div class="flex">
		<Card style="flex-grow: 1">
			<CardTitle>
				<Button style="margin: 0 auto">New Aggregate</Button>
			</CardTitle>
		</Card>
		<Card style="flex-grow: 1">
			<CardTitle>
				<Button style="margin: 0 auto">New Domain Event</Button>
			</CardTitle>
		</Card>
		<Card style="flex-grow: 1">
			<Select>REST Resources</Select>
			<Switch>Use CQRS</Switch>
			<Select>Storage Type</Select>
			<Select>Domain Model Database</Select>
			<Select>Command Model Database</Select>
			<Select>Query Model Database</Select>
			<Select>Projections</Select>
		</Card>
	</div>
</CardForm>

<style>
	.flex {
		display: flex;
		flex-direction: row;
	}
</style>