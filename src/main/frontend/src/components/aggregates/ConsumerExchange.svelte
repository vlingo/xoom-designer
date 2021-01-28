<script>
	import { Select,  TextField } from 'svelte-materialify/src';
  import DeleteButton from "./DeleteButton.svelte";
	import CreateButton from "./CreateButton.svelte";
	import { schemaRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';

  export let consumerExchangeName;
  export let receivers;
  export let methods;

	const addReceiver = () => {
		if(receivers.length > 0) {
			const lastReceiver = receivers[receivers.length-1];
			const schemaPrefix = lastReceiver.schema.split(":").splice(0, 3).join(":");
			const schemaPlaceholder = schemaPrefix + ":[Enter the schema name]:0.0.1";
			receivers = receivers.concat({ aggregateMethod: "", schema: schemaPlaceholder})
		} else {
			receivers = receivers.concat({ aggregateMethod: "", schema: "" })
		}
	}
	const deleteReceiver = (index) => { receivers.splice(index, 1); receivers = receivers; }


</script>

<h5>Consumer Exchange:</h5>
<TextField class="ma-2" bind:value={consumerExchangeName}>Exchange Name</TextField>

{#each receivers as receiver, i}
<span class="d-flex">
  <TextField class="ma-2" bind:value={receiver.schema} rules={[schemaRule]} validateOnBlur={!(receiver.schema)}>Schema Reference</TextField>
  <Select mandatory class="ma-2" items={formatArrayForSelect(methods.map(m => m.name))} bind:value={receiver.aggregateMethod}>Aggregate Method</Select>
  <DeleteButton title="Delete Schema" on:click={() => deleteReceiver(i)}/>
</span>
{/each}
<CreateButton title="Add Schema" on:click={addReceiver}/>
