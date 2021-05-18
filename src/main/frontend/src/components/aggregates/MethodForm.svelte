<script>
  import { TextField, Select, Switch } from "svelte-materialify/src";
	import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';
  import ErrorWarningTooltip from "./ErrorWarningTooltip.svelte";

  import List, { Item, Label } from '@smui/list';
  import Checkbox from '@smui/checkbox';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';

  export let method;
  export let stateFields;
  export let events;
  export let methods;

  let selectedEvent = method.event ? [method.event] : [];
  const symbols = ['*', '#', '+', '-']
  let menu;
  let anchor;
  let anchorClasses = {};

  $: selectedEvent, onSelectedEventChanged();

  function onSelectedEventChanged() {
    const lengthOfSelectedEvents = selectedEvent.length;
    method.event = lengthOfSelectedEvents > 0 ? selectedEvent[lengthOfSelectedEvents - 1] : '';
    selectedEvent = method.event ? [method.event] : [];
  }

  function updateParamaters(name, symbol = undefined) {
    const nameWithOrWithoutSymbol = symbol ? `${name} ${symbol}` : name;
    const indexOfAlreadyExists = method.parameters.findIndex(p => p === nameWithOrWithoutSymbol)
    if (indexOfAlreadyExists > -1) {
      method.parameters.splice(indexOfAlreadyExists, 1)
      method.parameters = method.parameters
      return;
    }
    var replace = `^${name}$|^${name} [*#+-]$`;
    var re = new RegExp(replace);
    const indexOfAnyExists = method.parameters.findIndex(p => {
      const a = p.search(re);
      return a !== -1;
    })
    if (indexOfAnyExists > -1) {
      method.parameters.splice(indexOfAnyExists, 1, nameWithOrWithoutSymbol)
      method.parameters = method.parameters
    } else {
      method.parameters = [...method.parameters, nameWithOrWithoutSymbol]
    }
  }

  function updateParamatersWithSymbol(fName, symbol) {
    const pName = `${fName} ${symbol}`
    if (method.parameters.includes(pName)) {
      method.parameters = []
    } else {
      method.parameters = [pName]
    }
  }

  $: validation = [requireRule(method.name), identifierRule(method.name), isPropertyUniqueRule(method.name, methods, 'name')].filter(v => v);
  $: isAnyCollectionParameterSelected = method.parameters.some(p => p.search(/ [*#+-]?/) > -1)
</script>

<div style="flex: 1;">
<div class="d-flex">
  <div class="mb-1 pb-1 mr-4" style="flex: 1;">
    <TextField bind:value={method.name} rules={[requireRule, identifierRule, (v) => isPropertyUniqueRule(v, methods, 'name')]} validateOnBlur={!method.name}>Name</TextField>
  </div>
  <div class="mb-1 pb-1 mr-4" style="flex: 1;">
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
    <div on:click={() => menu.setOpen(true)}
    >
      <Select
        value={method.parameters && method.parameters.length > 0 ? method.parameters.join(', ') : '(none)'}
        disabled={!stateFields.length}
      >
        Parameters
      </Select>
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
          {#each stateFields as field}
            <Item
              class="pa-0"
              on:SMUI:action={() => !field.collectionType && updateParamaters(field.name)}
              selected={method.parameters.includes(field.collectionType ? `${field.name} >` : field.name)}
              disabled={isAnyCollectionParameterSelected}
            >
              <Checkbox
                style="visibility: {field.collectionType ? 'hidden' : 'visible'}"
                disabled={isAnyCollectionParameterSelected || field.collectionType}
                checked={method.parameters.includes(field.collectionType ? `${field.name} >` : field.name)}
                value={field.collectionType ? `${field.name} >` : field.name} />
              <Label>{field.name} {field.collectionType ? '>' : ''}</Label>
            </Item>
            {#if field.collectionType}
              <div>
                {#each symbols as symbol}
                  <Item
                    class="pa-0 pl-10"
                    on:SMUI:action={() => updateParamatersWithSymbol(field.name, symbol)}
                    selected={method.parameters.includes(`${field.name} ${symbol}`)}
                  >
                    <Checkbox
                      checked={method.parameters.includes(`${field.name} ${symbol}`)}
                      value={`${field.name} ${symbol}`} />
                    <Label>{field.name} {symbol}</Label>
                  </Item>
                {/each}
              </div>
            {/if}
          {/each}
        </SelectionGroup>
      </List>
    </Menu>
    </div>
  </div>
  <div class="mb-1 pb-1 " style="flex: 1;">
    <Select disabled={!events.length} multiple closeOnClick={true} items={formatArrayForSelect(events.map(e => e.name))} bind:value={selectedEvent} placeholder="(none)">Event</Select>
  </div>
</div>
<div class="mb-3 pb-3 " style="flex: 1;">
  <Switch bind:checked={method.useFactory}>Involves creation of entity?</Switch>
</div>
</div>
<div style="align-self: flex-start;">
  <ErrorWarningTooltip
    type={validation && validation.length > 0 ? 'error' : 'warning'}
    messages={validation && validation.length > 0 ? validation : [method.parameters && method.parameters.length > 0 ? '' : 'are selected', selectedEvent && selectedEvent.length > 0 ? '' : 'is selected']}
    names={validation && validation.length > 0 ? ['Name', 'Name', 'Name'] : ['No paramaters', 'No event']}
  />
</div>