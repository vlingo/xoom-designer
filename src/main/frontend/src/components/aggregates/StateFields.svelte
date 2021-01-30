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
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">State Fields</h6>
  </legend>
  {#each stateFields as stateField, i (i)}
    <div class="d-flex">
      <div style="flex: 1;" class="mb-3 pb-4 mr-4">
        <TextField disabled={stateField.name === 'id'} autocomplete="off" bind:value={stateField.name} rules={[requireRule, identifierRule, isFieldNameUnique ]}>Name</TextField>
      </div>
      <div style="flex: 1;" class="mb-3 pb-4">
        <Select mandatory disabled={stateField.name === 'id'} items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
      </div>
      <div style="{stateFields.length > 1 ? 'width: 36px;' : ''}">
        {#if stateField.name !== 'id'}
          <DeleteButton title="Delete State Field" on:click={() => deleteStateField(i)}/>
        {/if}
      </div>
    </div>
  {/each}
  <CreateButton title="Add State Field" on:click={addStateField}/>
</fieldset>