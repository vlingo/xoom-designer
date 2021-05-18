<script>
  import { Select, TextField } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
  import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
	import { settings, simpleTypes, collectionTypes } from '../../stores';
  import FieldTypeSelect from './FieldTypeSelect.svelte';
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';

  export let stateFields;

  $: stateFieldsTypes =  formatArrayForSelect([...simpleTypes, ...$settings.model.valueObjectSettings.map(type => type.name)]);
  $: if (stateFieldsTypes) {
    stateFields = stateFields.map(f => {
      return {
        ...f,
        type: [...simpleTypes, ...$settings.model.valueObjectSettings.map(type => type.name)].includes(f.type) ? f.type : '',
        collectionType: f.collectionType === null || typeof f.collectionType === "string" ? f.collectionType : null
      }
    })
  }

	const addStateField = () => stateFields = stateFields.concat({ name: "", type: "", collectionType: "" });
  const deleteStateField = (index) => { stateFields.splice(index, 1); stateFields = stateFields; }
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">State Fields</h6>
  </legend>
  {#each stateFields as stateField, i (i)}
    <div class="d-flex">
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
        <!-- <Select mandatory disabled={i === 0} items={stateFieldsTypes} bind:value={stateField.type}>Type</Select> -->
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
          <DeleteWithDialog type="State Field" on:click={() => deleteStateField(i)}>
            <b>{stateField.name}</b> might be in use at events or methods sections.
          </DeleteWithDialog>
        {/if}
      </div>
    </div>
  {/each}
  <CreateButton title="Add State Field" on:click={addStateField}/>
</fieldset>