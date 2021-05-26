<script>
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import { classNameRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import Textfield from '@smui/textfield/Textfield.svelte';
  import { createEventDispatcher } from "svelte";

  import List, { Item, Label } from '@smui/list';
  import Checkbox from '@smui/checkbox';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';

  export let event;
  export let i;
  export let events;
  export let stateFields;

  let menu;
  let anchor;
  let anchorClasses = {}
  let selectedFields = [];

  const dispatch = createEventDispatcher();

  function remove() {
    dispatch('delete');
  }

  function updateFields(name) {
    const indexOfAlreadyExists = event.fields.findIndex(p => p === name)
    if (indexOfAlreadyExists > -1) {
      event.fields.splice(indexOfAlreadyExists, 1)
      event.fields = event.fields
    } else {
      event.fields = [...event.fields, name]
    }
  }

  $: selectedFields = event.fields && event.fields.length > 0 ? event.fields.join(', ') : '(none)';
</script>

<div class="d-flex align-center">
  <div style="flex: 1;" class="mb-3 pb-3 mr-4">
    <Textfield
      id="eventName{i}"
      style="width: 100%;"
      label="Name"
      required
      input$autocomplete="off"
      bind:value={event.name}
      invalid={[requireRule(event.name), classNameRule(event.name), isPropertyUniqueRule(event.name, events, 'name')].some(f => f)}>
    </Textfield>
  </div>
  <div style="flex: 1;" class="mb-3 pb-3">
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
    <div on:click={() => stateFields.length > 1 && menu.setOpen(true)}>
      <Textfield
        style="width: 100%;"
        value={selectedFields}
        disabled={!stateFields.length}
        label="Fields"
        input$readonly={true}
        on:keypress={(e) => {if(e.keyCode === 13 || e.key === 'Enter') menu.setOpen(true)}}
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
          {#each stateFields.filter(f => f.name && f.type) as field}
            {#if field.name !== 'id'}
              <Item
                class="pa-0"
                on:SMUI:action={() => updateFields(field.name)}
                selected={event.fields.includes(field.name)}
              >
                <Checkbox
                  checked={event.fields.includes(field.name)}
                  value={field.name} />
                <Label>{field.name}</Label>
              </Item>
            {/if}
          {/each}
        </SelectionGroup>
      </List>
    </Menu>
    </div>
  </div>
  <div>
    <ErrorWarningTooltip
      names={['Name', 'Name', 'Name']}
      messages={[requireRule(event.name), classNameRule(event.name), isPropertyUniqueRule(event.name, events, 'name')]}
    />
  </div>
  <div style="width: 36px;">
    <DeleteWithDialog type="Event" on:click={remove}>
      <b>{event.name}</b> might be in use at Methods and Producer Exchange sections.
    </DeleteWithDialog>
  </div>
</div>