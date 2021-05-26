<script>
	import { afterUpdate } from 'svelte';
  import DeleteButton from "./DeleteButton.svelte";
	import { schemaRule } from "../../validators";
	import FieldsetBox from './FieldsetBox.svelte';
	import Textfield from '@smui/textfield/Textfield.svelte';
	import Select, { Option } from '@smui/select';
	import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';

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
	<Textfield
		class="mb-4"
		style="width: 100%;"
		bind:value={consumerExchangeName}
		label="Exchange Name"
	></Textfield>
	{#each receivers as receiver, i}
		<div class="d-flex align-center">
			<div style="flex: 1;" class="mb-3 pb-3 mr-4">
				<Textfield
					style="width: 100%;"
					label="Schema Reference"
					bind:value={receiver.schema}
					invalid={[schemaRule(receiver.schema)].some(f => f)}
				></Textfield>
			</div>
			<div style="flex: 1;" mandatory class="mb-3 pb-3">
				<Select
					bind:value={receiver.aggregateMethod}
					label="Aggregate Method"
				>
					{#each methods.filter(f => f.name) as method}
						<Option value={method.name}>{method.name}</Option>
					{/each}
				</Select>
			</div>
			<div>
				<ErrorWarningTooltip
					names={['Schema Reference']}
					messages={[schemaRule(receiver.schema)]}
				/>
			</div>
			<div style="width: 36px;">
				<DeleteButton title="Delete Schema" on:click={() => deleteReceiver(i)}/>
			</div>
		</div>
	{/each}
</FieldsetBox>