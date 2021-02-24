<script>
  import { Select, TextField, Menu, Button, List, ListItem, Dialog } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
  import { identifierRule, requireRule, isPropertyUnique } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
	import { customTypes } from '../../stores';

  export let stateFields;
  export let aggregateType;
  let isNewTypeInputActive = false;
  let customFieldName = "";
  let stateTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char'];

  $: stateFieldsTypes =  formatArrayForSelect([...stateTypes, ...$customTypes]);

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
  const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
  const createCustomField = () => {
    customTypes.set([...$customTypes, customFieldName]);
    isNewTypeInputActive = false;
    customFieldName = "";
  };
  const isTypeUnique = (value) => [...stateTypes, ...$customTypes].some((item) => item === customFieldName) ? `${customFieldName} is already exist.` : undefined;

</script>

<div class="pb-4">
  <Menu offsetX={true}>
    <div slot="activator">
      <Button><span style="text-transform: none;">{aggregateType}State</span></Button>
    </div>
    <List style="min-width: 150px;">
      <ListItem on:click={() => isNewTypeInputActive = true}>New</ListItem>
      <ListItem>{aggregateType}State</ListItem>
      {#each $customTypes as type (type)}
        <ListItem>{type}</ListItem>
      {/each}
    </List>
  </Menu>

  <Dialog class="pa-8" bind:active={isNewTypeInputActive}>
    <TextField bind:value={customFieldName} rules={[isTypeUnique]}>Field Name</TextField>
    <Button on:click={createCustomField} disabled={customFieldName == "" || isTypeUnique(customFieldName)}>Add</Button>
  </Dialog>
</div>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">State Fields</h6>
  </legend>
  {#each stateFields as stateField, i (i)}
    <div class="d-flex">
      <div style="flex: 1;" class="mb-3 pb-4 mr-4">
        <TextField disabled={i === 0} autocomplete="off" bind:value={stateField.name} rules={[requireRule, identifierRule, (v) => isPropertyUnique(v, stateFields, 'name') ]}>Name</TextField>
      </div>
      <div style="flex: 1;" class="mb-3 pb-4">
        <Select mandatory disabled={i === 0} items={stateFieldsTypes} bind:value={stateField.type}>Type</Select>
      </div>
      <div style="{stateFields.length > 1 ? 'width: 36px;' : ''}">
        {#if i !== 0}
          <DeleteWithDialog type="State Field" on:click={() => deleteStateField(i)}>
            <b>{stateField.name}</b> might be in use at events or methods sections.
          </DeleteWithDialog>
        {/if}
      </div>
    </div>
  {/each}
  <CreateButton title="Add State Field" on:click={addStateField}/>
</fieldset>