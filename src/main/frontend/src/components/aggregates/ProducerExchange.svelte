<script>
  import { afterUpdate } from 'svelte';
  import { Select, TextField } from 'svelte-materialify/src';
	import { schemaGroupRule } from "../../validators";
  import { formatArrayForSelect } from "../../utils";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';

  export let events;
  export let producerExchangeName;
  export let outgoingEvents;
  export let schemaGroup;
  export let disableSchemaGroup;

  afterUpdate(() => {
    outgoingEvents = outgoingEvents.reduce((acc, cur) => {
      if (events.some(e => e.name === cur)) acc.push(cur);
      return acc;
    }, []);
	});
</script>

<FieldsetBox title="Producer Exchange">
  <span class="d-flex">
    <TextField class="mb-3 pb-3 mr-4" style="flex: 1;" bind:value={producerExchangeName}>Exchange Name</TextField>
    <TextField class="mb-3 pb-3" style="flex: 1;" bind:value={schemaGroup} rules={[schemaGroupRule]} validateOnBlur={!schemaGroup} disabled={disableSchemaGroup}>Organization : Unit : Context</TextField>
    <div>
      <ErrorWarningTooltip
        type='warning'
        messages={[producerExchangeName ? '' : 'Should you register any events for message publishing?']}
        names={['']}
      />
    </div>
  </span>
  <Select mandatory disabled={!events.length} multiple items={formatArrayForSelect(events.map(e => e.name))} bind:value={outgoingEvents}>Domain Event</Select>
</FieldsetBox>
