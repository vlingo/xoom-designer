<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import CardText from "svelte-materialify/src/components/Card/CardText.svelte";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	import Divider from "svelte-materialify/src/components/Divider";
	import Icon from "svelte-materialify/src/components/Icon";
	import { mdiDelete, mdiPencil } from "@mdi/js";
	import { createEventDispatcher } from "svelte";

	const dispatch = createEventDispatcher();

	export let aggregate;

	const methodParameters = (parameters) => parameters.map(p => aggregate.stateFields.find(sf => sf.name === p)).map(sf => sf.name + ': ' + sf.type);

	const edit = () => dispatch("edit");
	const remove = () => dispatch("remove");
</script>

<Card class="pa-3 ma-3">
	<div class="mb-2">
		{#each aggregate.api.routes as route}
			<Card class="command mb-1">
				<CardTitle>{route.httpMethod} {(!route.requireEntityLoad? aggregate.api.rootPath : aggregate.api.rootPath==="/"? "" : aggregate.api.rootPath) + route.path}</CardTitle>
			</Card>
		{/each}
	</div>
	<div class="mb-2">
		<Card class="aggregate">
			<CardTitle>
				<div>{aggregate.aggregateName}</div>
			</CardTitle>
			<Divider />
			<CardText>
				{#each aggregate.stateFields as field}
					<div>{field.name}: {field.type}</div>
				{/each}
				<Divider />
				{#each aggregate.methods as method}
					<div>{method.name}({methodParameters(method.parameters)})</div>
				{/each}
			</CardText>
		</Card>
	</div>
	{#each aggregate.events as event}
		<Card class="event mb-1">
			<CardTitle>{event.name}</CardTitle>
		</Card>
	{/each}
	<CardActions class="d-flex justify-space-around">
		<Button title="Edit Aggregate" on:click={edit} fab class="ma-2">
			<Icon path={mdiPencil}/>
		</Button>
		<Button title="Remove Aggregate" on:click={remove} fab class="ma-2 red-text">
			<Icon path={mdiDelete}/>
		</Button>
	</CardActions>
</Card>