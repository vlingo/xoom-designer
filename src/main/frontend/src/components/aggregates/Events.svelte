<script>
  import FieldsetBox from './FieldsetBox.svelte';
  import { tick } from "svelte";
  import Event from "./Event.svelte";

  export let events;
  export let stateFields;
  export let valueObjects;

	const addEvent = () => {
    events = events.concat({ name: "", fields: ["id"] })
    tick().then(() => {
      const el = document.querySelector(`#eventName${events.length - 1} input`);
      if (el) el.focus()
    })
  };
  const deleteEvent = (index) => {
    events.splice(index, 1);
    events = events;
    tick().then(() => {
      const el = document.querySelector(`#eventName${index === 0 ? 0 : index - 1} input`);
      if (el) el.focus()
    })
  }
  
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

<FieldsetBox title="Events" on:add={addEvent}>
  {#if events.length < 1}
    <div class="text-center">There is no event! Add one.</div>
  {/if}
  {#each events as event, i (i)}
    <Event bind:event {events} {i} {stateFields} {valueObjects} on:delete={() => deleteEvent(i)} />
  {/each}
</FieldsetBox>
