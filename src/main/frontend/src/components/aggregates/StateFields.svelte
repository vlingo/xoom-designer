<script>
  import { Select, TextField, Menu, Button, List, ListItem, Dialog } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
  import { identifierRule, requireRule, isPropertyUnique } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
	import { valueObjectTypes } from '../../stores';

  export let stateFields;
  export let aggregateType;
  let isValueObjectInputActive = false;
  let customFieldName = "";
  let simpleTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char', 'Date'];
  let updateValue = null;

  $: stateFieldsTypes =  formatArrayForSelect([...simpleTypes, ...$valueObjectTypes]);

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "" });
  const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
  const createValueObjectState = () => {
    $valueObjectTypes = [...$valueObjectTypes, customFieldName];
    reset();
  };
  const updateValueObjectState = () => {
    if (customFieldName !== updateValue) {
      $valueObjectTypes = $valueObjectTypes.filter(type => type !== updateValue);
      $valueObjectTypes = [...$valueObjectTypes, customFieldName];
    }
    reset();
  }
  const openDialogForCreate = () => {
    updateValue = null;
    isValueObjectInputActive = true;
  }
  const openDialogForUpdate = (value) => {
    updateValue = value;
    customFieldName = value;
    isValueObjectInputActive = true;
  }
  const reset = () => {
    isValueObjectInputActive = false;
    customFieldName = "";
    updateValue = null;
  }
  const isTypeUnique = (value) => {
    if (updateValue === value) return undefined;
    return [...simpleTypes, ...$valueObjectTypes].some((item) => item === value) ?  `${value} already exists.` : undefined;
  };

</script>

<div class="pb-4">
  <Menu offsetX={true}>
    <div slot="activator">
      <Button><span style="text-transform: none;">{aggregateType}State</span></Button>
    </div>
    <List style="min-width: 150px;">
      <ListItem class="primary-text" on:click={openDialogForCreate}>New</ListItem>
      {#each $valueObjectTypes as type (type)}
        <ListItem on:click={() => openDialogForUpdate(type)}>{type}</ListItem>
      {/each}
    </List>
  </Menu>

  <Dialog class="pa-8" bind:active={isValueObjectInputActive}>
    <TextField bind:value={customFieldName} rules={[isTypeUnique]}>Field Name</TextField>
    <div class="d-flex justify-space-between">
      <Button on:click={updateValue ? updateValueObjectState : createValueObjectState} disabled={customFieldName == "" || isTypeUnique(customFieldName)}>{updateValue ? 'Update' : 'Add'}</Button>
      <Button class="red white-text" on:click={reset}>Cancel</Button>
    </div>
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