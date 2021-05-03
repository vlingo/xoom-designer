<script>
  import { TextField, Select, Switch } from "svelte-materialify/src";
	import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';

  export let method;
  export let stateFields;
  export let events;
  export let methods;

  let selectedEvent = method.event ? [method.event] : [];

  $: selectedEvent, onSelectedEventChanged();

  function onSelectedEventChanged() {
    const lengthOfSelectedEvents = selectedEvent.length;
    method.event = lengthOfSelectedEvents > 0 ? selectedEvent[lengthOfSelectedEvents - 1] : '';
    selectedEvent = method.event ? [method.event] : [];
  }

</script>


<div class="d-flex">
  <div class="mb-1 pb-1 mr-4" style="flex: 1;">
    <TextField bind:value={method.name} rules={[requireRule, identifierRule, (v) => isPropertyUniqueRule(v, methods, 'name')]} validateOnBlur={!method.name}>Name</TextField>
  </div>
  <div class="mb-1 pb-1 mr-4" style="flex: 1;">
    <Select disabled={!stateFields.length} multiple items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={method.parameters} placeholder="(none)">Parameters</Select>
  </div>
  <div class="mb-1 pb-1 " style="flex: 1;">
    <Select disabled={!events.length} multiple closeOnClick={true} items={formatArrayForSelect(events.map(e => e.name))} bind:value={selectedEvent} placeholder="(none)">Event</Select>
  </div>
</div>
<div class="mb-3 pb-3 " style="flex: 1;">
  <Switch bind:checked={method.useFactory}>Involves creation of entity?</Switch>
</div>