<script>
	import { afterUpdate } from 'svelte';
	import { Select,  TextField } from 'svelte-materialify/src';
  import DeleteButton from "./DeleteButton.svelte";
	import { schemaRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
	import FieldsetBox from './FieldsetBox.svelte';

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

	afterUpdate(() => {
		receivers = receivers.map(receiver => {
			return {
				...receiver,
				aggregateMethod: methods.some(method => method.name === receiver.aggregateMethod) ? receiver.aggregateMethod : undefined
			}
		})
	});

</script>
<FieldsetBox title="Consumer Exchange" on:add={addReceiver}>
	<TextField class="mb-3 pb-3" bind:value={consumerExchangeName}>Exchange Name</TextField>
	{#each receivers as receiver, i}
		<div class="d-flex">
			<div style="flex: 1;" class="mb-3 pb-3 mr-4">
				<TextField bind:value={receiver.schema} rules={[schemaRule]} validateOnBlur={!(receiver.schema)}>Schema Reference</TextField>
			</div>
			<div style="flex: 1;" mandatory class="mb-3 pb-3">
				<Select items={formatArrayForSelect(methods.map(m => m.name))} bind:value={receiver.aggregateMethod}>Aggregate Method</Select>
			</div>
			<div style="width: 36px;">
				<DeleteButton title="Delete Schema" on:click={() => deleteReceiver(i)}/>
			</div>
		</div>
	{/each}
</FieldsetBox>