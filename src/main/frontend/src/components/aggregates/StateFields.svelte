<script>
	import CreateButton from "./CreateButton.svelte";
  import { formatArrayForSelect, uuid } from '../../utils';
	import { settings, simpleTypes } from '../../stores';
  import Sortable from '../Sortable.svelte';
  import StateField from './StateField.svelte';
  import { tick } from "svelte";

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

	const addStateField = () => {
    stateFields = [...stateFields, { name: "", type: "", collectionType: "", uid: uuid() }]
    tick().then(() => {
      document.querySelector(`#stateFieldName${stateFields.length - 1} input`).focus();
    })
  };
  const deleteStateField = (index) => {
    stateFields.splice(index, 1);
    stateFields = stateFields;
    tick().then(() => {
      document.querySelector(`#stateFieldName${index === 1 ? 1 : index - 1} input`).focus();
    })
  }
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">State Fields</h6>
  </legend>
  <StateField {stateFields} stateField={stateFields[0]} i={0} {stateFieldsTypes} />
  <Sortable
    options={{
			onSort: (d) => {
        const [field] = stateFields.splice(d.oldIndex + 1, 1);
        stateFields.splice(d.newIndex + 1, 0, field);
        stateFields = stateFields;
			},
    }}
  >
  {#each stateFields as stateField, i (stateField.uid)}
    {#if i > 0}
      <StateField {stateFields} bind:stateField i={i} {stateFieldsTypes} on:delete={() => deleteStateField(i)} />
    {/if}
  {/each}
  </Sortable>
  <CreateButton title="Add State Field" on:click={addStateField}/>
</fieldset>