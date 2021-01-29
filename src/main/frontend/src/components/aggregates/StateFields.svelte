<script>
  import { Select, TextField } from 'svelte-materialify/src';
  import DeleteButton from "./DeleteButton.svelte";
	import CreateButton from "./CreateButton.svelte";
  import { identifierRule, requireRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';

  export let stateFields;
  const stateFieldsTypes =  formatArrayForSelect(['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char']);

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
  const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
  
  const isFieldNameUnique = (value) => {
    const result = stateFields.filter(field => field.name === value);
		return result.length === 1 ? undefined : 'Name must be unique';
  }

  $: if (stateFields) {
    stateFields.forEach(field => {
      isFieldNameUnique(field.name);
    })
  }
</script>

<h5>State Fields:</h5>
{#each stateFields as stateField, i}
  <span class="d-flex">
  <div style="max-width: 100%">
    <TextField disabled={i<1} class="ma-2" bind:value={stateField.name} rules={[requireRule, identifierRule, isFieldNameUnique ]}>Name</TextField>
  </div>
  <div style="max-width: 100%">
    <Select mandatory disabled={i<1} class="ma-2" items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
  </div>
  {#if stateField.name !== 'id'}
    <DeleteButton title="Delete State Field" on:click={() => deleteStateField(i)}/>
  {/if}
  </span>
{/each}
<CreateButton title="Add State Field" on:click={addStateField}/>