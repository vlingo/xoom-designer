<script>
  import { afterUpdate } from 'svelte';
	import { schemaGroupRule } from "../../validators";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import FieldsetBox from './FieldsetBox.svelte';
  import Textfield from '@smui/textfield/Textfield.svelte';
  import HelperText from '@smui/textfield/helper-text/index';

  import List, { Item, Label } from '@smui/list';
  import Checkbox from '@smui/checkbox';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';

  let menu;
  let anchor;
  let anchorClasses = {}
  let selectedEvents = [];

  export let events;
  export let producerExchangeName;
  export let outgoingEvents;
  export let schemaGroup;
  export let disableSchemaGroup;

  function updateOutgoingEvents(name) {
    const indexOfAlreadyExists = outgoingEvents.findIndex(p => p === name)
    if (indexOfAlreadyExists > -1) {
      outgoingEvents.splice(indexOfAlreadyExists, 1)
      outgoingEvents = outgoingEvents
    } else {
      outgoingEvents = [...outgoingEvents, name]
    }
  }

  afterUpdate(() => {
    outgoingEvents = outgoingEvents.reduce((acc, cur) => {
      if (events.some(e => e.name === cur)) acc.push(cur);
      return acc;
    }, []);
	});

  $: selectedEvents = outgoingEvents && outgoingEvents.length > 0 ? outgoingEvents.join(', ') : '(none)';
</script>

<FieldsetBox title="Producer Exchange" addable={false}>
  <div class="d-flex">
    <Textfield
      class="mr-4"
      style="flex: 1;"
      label="Exchange Name"
      bind:value={producerExchangeName}
      invalid={!producerExchangeName && (schemaGroup || outgoingEvents.length > 0)}
    />
    <div style="flex: 1;">
      <Textfield
        style="width: 100%;"
        label="Organization : Unit : Context"
        disabled={disableSchemaGroup}
        bind:value={schemaGroup}
        invalid={[schemaGroupRule(schemaGroup)].some(f => f) && (producerExchangeName || outgoingEvents.length > 0)}
      >
        <HelperText persistent slot="helper">{schemaGroupRule(schemaGroup)}</HelperText>
      </Textfield>
    </div>

    <div>
      <ErrorWarningTooltip
        type='warning'
        messages={[producerExchangeName ? '' : 'Should you register any events for message publishing?']}
        names={['']}
      />
    </div>
  </div>
  <div>
    <div
      class={Object.keys(anchorClasses).join(' ')}
      use:Anchor={{
        addClass: (className) => {
          if (!anchorClasses[className]) {
            anchorClasses[className] = true;
          }
        },
        removeClass: (className) => {
          if (anchorClasses[className]) {
            delete anchorClasses[className];
            anchorClasses = anchorClasses;
          }
        },
      }}
      bind:this={anchor}
    >
    <div on:click={() => events.length && menu.setOpen(true)}>
      <Textfield
        style="width: 100%;"
        value={selectedEvents}
        disabled={!events.length}
        label="Domain Event"
        input$readonly={true}
        on:keypress={(e) => {if(e.keyCode === 13 || e.key === 'Enter') menu.setOpen(true)}}
        invalid={outgoingEvents.length < 1 && (producerExchangeName || schemaGroup)}
      ></Textfield>
    </div>
    <Menu
      bind:this={menu}
      anchor={false}
      bind:anchorElement={anchor}
      anchorCorner="BOTTOM_LEFT"  
      style="width: 100%;"
    >
      <List class="demo-list" checkList>
        <SelectionGroup>
          {#each events.filter(e => e.name) as event}
            <Item
              class="pa-0"
              on:SMUI:action={() => updateOutgoingEvents(event.name)}
              selected={outgoingEvents.includes(event.name)}
            >
              <Checkbox
                checked={outgoingEvents.includes(event.name)}
                value={event.name} />
              <Label>{event.name}</Label>
            </Item>
          {/each}
        </SelectionGroup>
      </List>
    </Menu>
    </div>
  </div>
</FieldsetBox>
