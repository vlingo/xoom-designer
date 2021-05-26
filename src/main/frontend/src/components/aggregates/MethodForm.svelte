<script>
  import { Switch } from "svelte-materialify/src";
	import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import ErrorWarningTooltip from "./ErrorWarningTooltip.svelte";

  import List, { Item, Label } from '@smui/list';
  import Checkbox from '@smui/checkbox';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';
  import pluralize from 'pluralize';
  import Textfield from "@smui/textfield/Textfield.svelte";

  export let method;
  export let stateFields;
  export let events;
  export let methods;
  export let i;

  const symbols = [{
    sign: '*',
    isPlural: true,
  }, {
    sign: '#',
    isPlural: true,
  }, {
    sign: '+',
    isPlural: false,
  }, {
    sign: '-',
    isPlural: false,
  }];
  let menu;
  let anchor;
  let anchorClasses = {};

  let menuEvent;
  let anchorEvent;
  let anchorClassesEvent = {};

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

  function updateEvent(eName) {
    if (eName === method.event) {
      method.event = null
    } else {
      method.event = eName;
    }
  }

  $: validation = [requireRule(method.name), identifierRule(method.name), isPropertyUniqueRule(method.name, methods, 'name')].filter(v => v);
  $: isAnyCollectionParameterSelected = method.parameters.some(p => p.search(/ [*#+-]$/) > -1)
</script>

<div style="flex: 1;">
<div class="d-flex align-center">
  <div class="mb-1 pb-1 mr-4" style="flex: 1;">
    <Textfield
      id="methodName{i}"
      style="width: 100%;"
      label="Name"
      required
      input$autocomplete="off"
      bind:value={method.name}
      rules={[requireRule, identifierRule, (v) => isPropertyUniqueRule(v, methods, 'name')]}
    >
    </Textfield>
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
      <Textfield
        style="width: 100%;"
        value={method.parameters && method.parameters.length > 0 ? method.parameters.join(', ') : '(none)'}
        disabled={!stateFields.length}
        label="Parameters"
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
          {#each stateFields as field}
            <Item
              class="pa-0"
              on:SMUI:action={() => !field.collectionType && updateParamaters(field.name)}
              selected={method.parameters.includes(field.collectionType ? `${field.name} >` : field.name)}
              disabled={isAnyCollectionParameterSelected || field.collectionType}
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
                    on:SMUI:action={() => updateParamatersWithSymbol(symbol.isPlural ? field.name : pluralize.singular(field.name), symbol.sign)}
                    selected={method.parameters.includes(`${symbol.isPlural ? field.name : pluralize.singular(field.name)} ${symbol.sign}`)}
                  >
                    <Checkbox
                      checked={method.parameters.includes(`${symbol.isPlural ? field.name : pluralize.singular(field.name)} ${symbol.sign}`)}
                      value={`${symbol.isPlural ? field.name : pluralize.singular(field.name)} ${symbol.sign}`} />
                    <Label>{symbol.isPlural ? field.name : pluralize.singular(field.name)} {symbol.sign}</Label>
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
    <div
      class={Object.keys(anchorClassesEvent).join(' ')}
      use:Anchor={{
        addClass: (className) => {
          if (!anchorClassesEvent[className]) {
            anchorClassesEvent[className] = true;
          }
        },
        removeClass: (className) => {
          if (anchorClassesEvent[className]) {
            delete anchorClassesEvent[className];
            anchorClassesEvent = anchorClassesEvent;
          }
        },
      }}
      bind:this={anchorEvent}
    >
    <div on:click={() => menuEvent.setOpen(true)}>
      <Textfield
        style="width: 100%;"
        value={method.event ? method.event : '(none)'}
        disabled={!stateFields.length}
        label="Event"
        input$readonly={true}
        on:keypress={(e) => {if(e.keyCode === 13 || e.key === 'Enter') menuEvent.setOpen(true)}}
      ></Textfield>
    </div>
    <Menu
      bind:this={menuEvent}
      anchor={false}
      bind:anchorElement={anchorEvent}
      anchorCorner="BOTTOM_LEFT"  
      style="width: 100%;"
    >
      <List class="demo-list" checkList>
        <SelectionGroup>
          {#each events as event}
            <Item
              class="pa-0"
              on:SMUI:action={() => updateEvent(event.name)}
              selected={method.event === event.name}
            >
              <Checkbox
                checked={method.event === event.name}
                value={event.name} />
              <Label>{event.name}</Label>
            </Item>
          {/each}
        </SelectionGroup>
      </List>
    </Menu>
    </div>
  </div>
</div>
<div class="mb-3 pb-3 " style="flex: 1;">
  <Switch bind:checked={method.useFactory}>Involves creation of entity?</Switch>
</div>
</div>
<div style="align-self: flex-start;">
  <ErrorWarningTooltip
    type={validation && validation.length > 0 ? 'error' : 'warning'}
    messages={validation && validation.length > 0 ? validation : [method.parameters && method.parameters.length > 0 ? '' : 'are selected', method.event ? '' : 'is selected']}
    names={validation && validation.length > 0 ? ['Name', 'Name', 'Name'] : ['No paramaters', 'No event']}
  />
</div>