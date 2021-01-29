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

<h5>Events:</h5>
{#each events as event, i}
  <span class="d-flex">
    <TextField class="ma-2" bind:value={event.name} rules={[requireRule, classNameRule, isEventNameUnique]} validateOnBlur={!event.name}>Name</TextField>
    <Select id="event-fields" mandatory disabled={!stateFields.length} multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={event.fields}>Fields</Select>
    <DeleteButton title="Delete Event" on:click={() => deleteEvent(i)}/>
  </span>
{/each}
<CreateButton title="Add Event" on:click={addEvent}/>