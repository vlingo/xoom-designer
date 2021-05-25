<script>
  import { Select, TextField, Icon } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
  import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
	import { collectionTypes } from '../../stores';
  import FieldTypeSelect from './FieldTypeSelect.svelte';
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import { mdiArrowUpDown, mdiArrowVerticalLock } from '@mdi/js';
  import { createEventDispatcher } from 'svelte';

  export let stateFields;
  export let stateField;
  export let stateFieldsTypes;
  export let i;

  const dispatch = createEventDispatcher();

  function remove() {
    dispatch('delete')
  }
</script>

<div class="d-flex">
  <div
    class="handle pa-2"
    class:disabled={i === 0}
    style="width: 42px; cursor: {i === 0 ? 'not-allowed' : 'move'};"
  >
    <Icon path={i === 0 ? mdiArrowVerticalLock : mdiArrowUpDown}/>
  </div>
  <div style="flex: 1;" class="mb-3 pb-4 mr-4">
    <TextField disabled={i === 0} autocomplete="off" bind:value={stateField.name} rules={[requireRule, identifierRule, (v) => isPropertyUniqueRule(v, stateFields, 'name') ]}>Name</TextField>
  </div>
  <div style="flex: 1;" class="mb-3 pb-4 mr-4">
    <FieldTypeSelect
      mandatory
      disabled={i === 0}
      items={stateFieldsTypes}
      bind:value={stateField.type}
      collectionType={stateField.collectionType}
    >
      Type
    </FieldTypeSelect>
  </div>
  <div style="flex: 1;" class="mb-3 pb-4">
    <Select disabled={i === 0} items={formatArrayForSelect(collectionTypes.map(f => f.name))}  bind:value={stateField.collectionType} placeholder="(bare)">Collection</Select>
  </div>
  <div>
    <ErrorWarningTooltip
      names={['Name', 'Name', 'Name', 'Type']}
      messages={[requireRule(stateField.name), identifierRule(stateField.name), isPropertyUniqueRule(stateField.name, stateFields, 'name') ,requireRule(stateField.type)]}
    />
  </div>
  <div style="{stateFields.length > 1 ? 'width: 36px;' : ''}">
    {#if i !== 0}
      <DeleteWithDialog type="State Field" on:click={remove}>
        <b>{stateField.name}</b> might be in use at events or methods sections.
      </DeleteWithDialog>
    {/if}
  </div>
</div>