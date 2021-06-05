<script>
  import { Switch } from "svelte-materialify/src";
	import { identifierRule, requireRule, isPropertyUniqueRule, methodParametersValidityWithSelectedEventRule } from "../../validators";
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
  let selectedParameters = '';
  let isAnyCollectionParameterSelected = false;
  function updateParamaters(name) {
    const indexOfAlreadyExists = method.parameters.findIndex(p => p.stateField === name && !p.multiplicity)
    if (indexOfAlreadyExists > -1) {
      method.parameters.splice(indexOfAlreadyExists, 1)
      method.parameters = method.parameters
      return;
    }
    const param = {
      stateField: name,
      parameterName: name,
      multiplicity: ''
    }
    if (isAnyCollectionParameterSelected) {
      method.parameters = [param]
    } else {
      method.parameters = [...method.parameters, param]
    }
  }

  function updateParamatersWithSymbol(fName, symbol) {
    const param = {
      stateField: fName,
      parameterName: symbol.isPlural ? fName : pluralize.singular(fName),
      multiplicity: symbol.sign
    }
    const p = method.parameters.find(p => p.stateField === fName && p.multiplicity === symbol.sign)
    if (p) {
      method.parameters = []
    } else {
      method.parameters = [param]
    }
  }

  function updateEvent(eName) {
    if (eName === method.event) {
      method.event = null
    } else {
      method.event = eName;
    }
  }

  $: validation = [requireRule(method.name), identifierRule(method.name), isPropertyUniqueRule(method.name, methods, 'name'), methodParametersValidityWithSelectedEventRule(method.event, events, method.parameters)];
  $: isAnyCollectionParameterSelected = method.parameters.some(p => p.multiplicity);
  $: selectedParameters = method.parameters && method.parameters.length > 0 ? isAnyCollectionParameterSelected ? `${method.parameters[0].parameterName} ${method.parameters[0].multiplicity}` : method.parameters.map(p => p.parameterName).join(', ') : '(none)';
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
      invalid={[requireRule(method.name), identifierRule(method.name), isPropertyUniqueRule(method.name, methods, 'name')].some(f => f)}
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
    <div on:click={() => stateFields.length > 0 && menu.setOpen(true)}
    >
      <Textfield
        style="width: 100%;"
        value={selectedParameters}
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
          {#each stateFields.filter(f => f.name && f.type) as field}
            <Item
              class="pa-0"
              on:SMUI:action={() => !field.collectionType && updateParamaters(field.name)}
              selected={method.parameters.findIndex(p => p.stateField === field.name) > -1}
              disabled={isAnyCollectionParameterSelected || field.collectionType}
            >
              <Checkbox
                style="visibility: {field.collectionType ? 'hidden' : 'visible'}"
                disabled={isAnyCollectionParameterSelected || field.collectionType}
                checked={method.parameters.some(p => p.stateField === field.name)}
                value={field.collectionType ? `` : field.name}
              />
              <Label>{field.name} {field.collectionType ? '>' : ''}</Label>
            </Item>
            {#if field.collectionType}
              <div>
                {#each symbols as symbol}
                  <Item
                    class="pa-0 pl-10"
                    on:SMUI:action={() => updateParamatersWithSymbol(field.name, symbol)}
                    selected={method.parameters.some(p => p.stateField === field.name && p.multiplicity === symbol.sign)}
                  >
                    <Checkbox
                      checked={method.parameters.some(p => p.stateField === field.name && p.multiplicity === symbol.sign)}
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
    <div on:click={() => events.length > 0 && menuEvent.setOpen(true)}>
      <Textfield
        style="width: 100%;"
        value={method.event ? method.event : '(none)'}
        disabled={!events.length}
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
          {#each events.filter(f => f.name) as event}
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
    type={validation && validation.filter(v => v).length > 0 ? 'error' : 'warning'}
    messages={validation && validation.filter(v => v).length > 0 ? validation : [method.parameters && method.parameters.length > 0 ? '' : 'are selected', method.event ? '' : 'is selected']}
    names={validation && validation.filter(v => v).length > 0 ? ['Name', 'Name', 'Name', 'Parameters'] : ['No paramaters', 'No event']}
  />
</div>