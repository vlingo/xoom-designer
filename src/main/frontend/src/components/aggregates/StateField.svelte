<script>
  import { Icon } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
  import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import FieldTypeSelect from './FieldTypeSelect.svelte';
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import { mdiArrowUpDown, mdiArrowVerticalLock } from '@mdi/js';
  import { createEventDispatcher } from 'svelte';
  import Textfield from '@smui/textfield';
  import CollectionTypeSelect from './CollectionTypeSelect.svelte';

  export let stateFields;
  export let stateField;
  export let stateFieldsTypes;
  export let i;

  const dispatch = createEventDispatcher();

  function remove() {
    dispatch('delete')
  }
</script>

<div class="d-flex align-center">
  <div
    class="handle pa-2"
    class:disabled={i === 0}
    style="width: 42px; cursor: {i === 0 ? 'not-allowed' : 'move'};"
  >
    <Icon path={i === 0 ? mdiArrowVerticalLock : mdiArrowUpDown}/>
  </div>
  <div style="flex: 1;" class="pb-4 mr-4">
    <Textfield
      id="stateFieldName{i}"
      style="width: 100%;"
      label="Name"
      required
      disabled={i === 0}
      input$autocomplete="off"
      bind:value={stateField.name}
      rules={[requireRule, identifierRule, (v) => isPropertyUniqueRule(v, stateFields, 'name') ]}
    >
    </Textfield>
  </div>
  <div style="flex: 1;" class="pb-4 mr-4">
    <FieldTypeSelect
      disabled={i === 0}
      items={stateFieldsTypes}
      bind:value={stateField.type}
      collectionType={stateField.collectionType}
    >
      Type
    </FieldTypeSelect>
  </div>
  <div style="flex: 1;" class="pb-4">
    <CollectionTypeSelect disabled={i === 0} bind:value={stateField.collectionType} />
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