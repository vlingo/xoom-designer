<script>
  import { Select, TextField } from 'svelte-materialify/src';
  import DeleteButton from "./DeleteButton.svelte";
	import CreateButton from "./CreateButton.svelte";
	import { classNameRule, requireRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';

  export let events;
  export let stateFields;

	const addEvent = () => events = events.concat({ name: "", fields: ["id"] });
  const deleteEvent = (index) => { events.splice(index, 1); events = events; }
  
  $: {
		events = events.map((event) => {
			return {
				...event,
				fields: stateFields.reduce((acc, cur) => {
					if (event.fields && event.fields.includes(cur.name)) acc.push(cur.name);
					return acc;
				}, [])
			};
		})
	}

	const isEventNameUnique = (value) => {
    const result = events.filter(event => event.name === value);
		return result.length === 1 ? undefined : 'Name must be unique';
  }
  
  $: if (events) {
    events.forEach(event => {
      isEventNameUnique(event.name);
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
  {#each events as event, i}
    <div class="d-flex">
      <div style="flex: 1;" class="mb-3 pb-3 mr-4">
        <TextField  bind:value={event.name} rules={[requireRule, classNameRule, isEventNameUnique]} validateOnBlur={!event.name}>Name</TextField>
      </div>
      <div style="flex: 1;" class="mb-3 pb-3">
        <Select mandatory disabled={!stateFields.length} multiple items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={event.fields}>Fields</Select>
      </div>
      <div style="width: 36px;">
        <DeleteButton title="Delete Event" on:click={() => deleteEvent(i)}/>
      </div>
    </div>
  {/each}
  <CreateButton title="Add Event" on:click={addEvent}/>
</fieldset>
