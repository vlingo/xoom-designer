<script>
	import { afterUpdate, tick } from 'svelte';
  import DeleteButton from "./DeleteButton.svelte";
	import { requireRule, schemaRule } from "../../validators";
	import FieldsetBox from './FieldsetBox.svelte';
	import Textfield from '@smui/textfield/Textfield.svelte';
	import Select, { Option } from '@smui/select';
	import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
	import ConsumerSchemata from './ConsumerSchemata.svelte';

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
		tick().then(() => {
      const el = document.querySelector(`#schemaRefName${receivers.length - 1} input`);
      if (el) el.focus()
    })
	}
	const deleteReceiver = (index) => {
		receivers.splice(index, 1);
		receivers = receivers;
		tick().then(() => {
      const el = document.querySelector(`#schemaRefName${index === 0 ? 0 : index - 1} input`);
      if (el) el.focus()
    })
	}

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
	<div class="d-flex align-center">
		<Textfield
			class="mb-4"
			style="width: 100%;"
			bind:value={consumerExchangeName}
			label="Exchange Name"
			invalid={!consumerExchangeName && receivers.length > 0}
		></Textfield>
		<ErrorWarningTooltip
			names={['Exchange Name']}
			messages={[!consumerExchangeName && receivers.length > 0 ? 'must not be empty' : '']}
		/>
	</div>
	{#each receivers as receiver, i (i)}
		<div class="d-flex align-center">
			<div
				style="flex: 2;"
				class="mb-3 pb-3 mr-4" 
				id="schemaRefName{i}"
			>
				<ConsumerSchemata
					bind:schema={receiver.schema}
					invalid={[schemaRule(receiver.schema)].some(f => f)}
				/>
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
					names={['Schema Reference', 'Schema Reference', 'Aggregate Method']}
					messages={[requireRule(receiver.schema), schemaRule(receiver.schema), requireRule(receiver.aggregateMethod)]}
				/>
			</div>
			<div style="width: 36px;">
				<DeleteButton title="Delete Schema" on:click={() => deleteReceiver(i)}/>
			</div>
		</div>
	{/each}
</FieldsetBox>