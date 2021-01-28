<script>
  import { Select, TextField } from 'svelte-materialify/src';
	import { schemaGroupRule } from "../../validators";
  import { formatArrayForSelect } from "../../utils";

  export let events;
  export let producerExchangeName;
  export let outgoingEvents;
  export let schemaGroup;
  export let disableSchemaGroup;
</script>

<h5>Producer Exchange:</h5>
<span class="d-flex">
  <TextField class="ma-2" bind:value={producerExchangeName}>Exchange Name</TextField>
  <TextField class="ma-2" bind:value={schemaGroup} rules={[schemaGroupRule]} validateOnBlur={!schemaGroup} disabled={disableSchemaGroup}>Organization : Unit : Context</TextField>
</span>
<Select mandatory disabled={!events.length} multiple class="ma-2" items={formatArrayForSelect(events.map(e => e.name))} bind:value={outgoingEvents}>Domain Event</Select>
