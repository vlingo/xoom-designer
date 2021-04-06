<script>
  import { Select, TextField } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
	import { classNameRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
  import FillFieldsNote from './FillFieldsNote.svelte';

  export let events;
  export let stateFields;

	const addEvent = () => events = events.concat({ name: "", fields: ["id"] });
  const deleteEvent = (index) => { events.splice(index, 1); events = events; }
  
  $: {
		events = events.map((event) => {
			return {
				...event,
				fields: stateFields.reduce((acc, cur) => {
					if (event.fields && event.fields.includes(cur.name) && acc.findIndex(a => a === cur.name) < 0) acc.push(cur.name);
					return acc;
				}, [])
			};
		})
	}
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">Events</h6>
  </legend>
  {#if events.length < 1}
    <div class="text-center">There is no event! Add one.</div>
  {/if}
  {#each events as event, i (i)}
    <div class="d-flex">
      <div style="flex: 1;" class="mb-3 pb-3 mr-4">
        <TextField  bind:value={event.name} rules={[requireRule, classNameRule, (v) => isPropertyUniqueRule(v, events, 'name')]} validateOnBlur={!event.name}>Name</TextField>
      </div>
      <div style="flex: 1;" class="mb-3 pb-3">
        <Select mandatory disabled={!stateFields.length} multiple items={formatArrayForSelect(stateFields.map(f => f.name !== 'id' && f.name))} bind:value={event.fields}>Fields</Select>
      </div>
      <div style="width: 36px;">
        <DeleteWithDialog type="Event" on:click={() => deleteEvent(i)}>
          <b>{event.name}</b> might be in use at Methods and Producer Exchange sections.
        </DeleteWithDialog>
      </div>
    </div>
  {/each}
  {#if events.filter(event => requireRule(event.name) || classNameRule(event.name) || isPropertyUniqueRule(event.name, events, 'name') || event.fields.length < 1).length > 0}
    <FillFieldsNote />
  {/if}
  <CreateButton title="Add Event" on:click={addEvent}/>
</fieldset>
