<script>
  import { Select, TextField } from 'svelte-materialify/src';
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import { classNameRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';

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

<FieldsetBox title="Events" on:add={addEvent}>
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
      <div>
        <ErrorWarningTooltip
          names={['Name', 'Name', 'Name']}
          messages={[requireRule(event.name), classNameRule(event.name), isPropertyUniqueRule(event.name, events, 'name')]}
        />
      </div>
      <div style="width: 36px;">
        <DeleteWithDialog type="Event" on:click={() => deleteEvent(i)}>
          <b>{event.name}</b> might be in use at Methods and Producer Exchange sections.
        </DeleteWithDialog>
      </div>
    </div>
  {/each}
</FieldsetBox>
