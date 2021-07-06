<script>
  import { formatArrayForSelect, uuid } from '../../utils';
	import { settings, simpleTypes, valueObjectNameChanges } from '../../stores';
  import Sortable from '../Sortable.svelte';
  import StateField from './StateField.svelte';
  import { tick } from "svelte";
  import FieldsetBox from "./FieldsetBox.svelte";

  export let stateFields;
  let stateFieldsTypes;
  
  $: {
    stateFieldsTypes =  formatArrayForSelect([...simpleTypes, ...$settings.model.valueObjectSettings.map(type => type.name)]);
  }
  $: if (stateFieldsTypes) {
    stateFields = stateFields.map(f => {
      return {
        ...f,
        type: [...simpleTypes, ...$settings.model.valueObjectSettings.map(type => type.name)].includes(f.type) ? f.type : valueObjectNameChanges.currentNameOf(f.type),
        collectionType: f.collectionType === null || typeof f.collectionType === "string" ? f.collectionType : null
      }
    })
    valueObjectNameChanges.finishWith($settings);
  }

	const addStateField = () => {
    stateFields = [...stateFields, { name: "", type: "", collectionType: "", uid: uuid() }]
    tick().then(() => {
      const el = document.querySelector(`#stateFieldName${stateFields.length - 1} input`);
      if (el) el.focus()
    })
  };
  const deleteStateField = (index) => {
    stateFields.splice(index, 1);
    stateFields = stateFields;
    tick().then(() => {
      const el = document.querySelector(`#stateFieldName${index === 1 ? 1 : index - 1} input`);
      if (el) el.focus()
    })
  }
</script>

<FieldsetBox title="State Fields" on:add={addStateField}>
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
</FieldsetBox>