<script>
	import Button from "svelte-materialify/src/components/Button";
	import Card from "svelte-materialify/src/components/Card";
	import CardActions from "svelte-materialify/src/components/Card/CardActions.svelte";
	import CardText from "svelte-materialify/src/components/Card/CardText.svelte";
	import CardTitle from "svelte-materialify/src/components/Card/CardTitle.svelte";
	import Divider from "svelte-materialify/src/components/Divider";
	import Icon from "svelte-materialify/src/components/Icon";
	import { mdiPencil } from "@mdi/js";
	import { createEventDispatcher } from "svelte";
	import DeleteWithDialog from "./DeleteWithDialog.svelte";

	const dispatch = createEventDispatcher();

	export let aggregate;

	const methodParameters = (parameters) => parameters.map(p => aggregate.stateFields.find(sf => sf.name === p)).map(sf => sf.name + ': ' + sf.type).join(", ");

	const fullRoute = (route) => {
		return (
			route.httpMethod + " " +
			aggregate.api.rootPath +
			route.path
		).replaceAll("//", "/")  + "->" + route.aggregateMethod;
	};

	const fullReceiver = (receiver) => {
		return receiver.schema.split(":")[3] + "->" + receiver.aggregateMethod;
	};

	const edit = () => dispatch("edit");
	const remove = () => dispatch("remove");
</script>

<Card class="pa-3 ma-3" style="min-width: 15rem; min-height: 20rem">
	<div class="d-flex flex-column" style="height: 100%">
	<div class="mb-2">
		{#each aggregate.api.routes as route}
			<Card class="command mb-1">
				<CardTitle>{fullRoute(route)}</CardTitle>
			</Card>
		{/each}
		{#each aggregate.consumerExchange.receivers as receiver}
		<Card class="event mb-1">
			<CardTitle>{fullReceiver(receiver)}</CardTitle>
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

	<CardActions style="margin-top: auto" class="justify-space-around">
		<Button title="Edit Aggregate" on:click={edit} icon class="ma-2">
			<Icon path={mdiPencil}/>
		</Button>
		<DeleteWithDialog type="Aggregate" on:click={remove}/>
	</CardActions>
</div>
</Card>