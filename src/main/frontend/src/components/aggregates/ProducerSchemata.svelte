<script>
  import { onMount } from "svelte";
  import IconButton from '@smui/icon-button';
  import { schemataData } from "../../stores";
  import Textfield from '@smui/textfield/Textfield.svelte';
  import Button from '@smui/button';

  import Menu, { SelectionGroup } from '@smui/menu';
  import List, { Item, Text } from '@smui/list';
  import { Anchor } from '@smui/menu-surface';
  import SchemataEmded from "./SchemataEmded.svelte";

  let anchor;
  let anchorClasses = {};
  let menu;

  export let disableSchemaGroup;
  export let schemaGroup;
  export let invalid;

  let schemaGroupParsed = schemaGroup && schemaGroup.split(':');
  let selectedOrg = schemaGroupParsed ? $schemataData.organizationsStore.find(org => org.name === schemaGroupParsed[0]) : null;
  let selectedUnit = schemaGroupParsed ? $schemataData.unitsStore.find(unit => unit.name === schemaGroupParsed[1]) : null;
  let selectedContext = schemaGroupParsed ? $schemataData.contextsStore.find(context => context.namespace === schemaGroupParsed[2]) : null;

  let showSchemataModal = false;

$: schemaGroup = `${selectedOrg ? selectedOrg.name : ''}${selectedUnit ? `:${selectedUnit.name}` : ''}${selectedContext ? `:${selectedContext.namespace}` : ''}`;
$: if(!selectedOrg) selectedUnit = null;
$: if(!selectedUnit) selectedContext = null;
</script>
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
<Textfield
  style="width: 100%;"
  label="Organization : Unit : Context"
  disabled={disableSchemaGroup}
  value={schemaGroup}
  input$readonly={true}
  {invalid}
  on:click={() => menu.setOpen(true)}
>
  <svelte:fragment slot="trailingIcon">
    {#if selectedOrg}
      <IconButton class="material-icons" style="color: red !important;" on:click={(e) => {
        e.stopPropagation();
        e.preventDefault();
        selectedOrg = null;
      }}>delete</IconButton>
    {/if}
  </svelte:fragment>
</Textfield>
<Menu
  bind:this={menu}
  anchor={false}
  bind:anchorElement={anchor}
  anchorCorner="BOTTOM_LEFT"
  style="width: 100%;"
>
  <List style="display: flex; justify-content: space-between;">
    <SelectionGroup style="flex: 1;">
      <Item class="pl-4 justify-center" disabled={true}>
        <Text>Organization</Text>
      </Item>
      {#each $schemataData.organizationsStore as org}
        <Item
          class="pl-4 justify-center"
          on:SMUI:action={() => {
            if (!selectedOrg || selectedOrg.organizationId !== org.organizationId) {
              selectedOrg = org;
              selectedUnit = null;
              selectedContext = null;
            }
          }}
          selected={selectedOrg && selectedOrg.organizationId === org.organizationId}
        >
          <Text>{org.name}</Text>
        </Item>
      {/each}
  </SelectionGroup>
    {#if selectedOrg}
      <div class="bl-1"></div>
      <SelectionGroup style="flex: 1;">
        <Item class="pl-4 justify-center" disabled={true}>
          <Text>Unit</Text>
        </Item>
        {#each $schemataData.unitsStore.filter(u => u.organizationId === selectedOrg.organizationId) as unit}
          <Item
            class="pl-4 justify-center"
            on:SMUI:action={() => {
              if (!selectedUnit || selectedUnit.unitId !== unit.unitId) {
                selectedUnit = unit;
                selectedContext = null;
              }
            }}
            selected={selectedUnit && selectedUnit.unitId === unit.unitId}
          >
            <Text>{unit.name}</Text>
          </Item>
        {/each}
      </SelectionGroup>
    {/if}
    {#if selectedUnit}
      <div class="bl-1"></div>
      <SelectionGroup style="flex: 1;">
        <Item class="pl-4 justify-center" disabled={true}>
          <Text>Context</Text>
        </Item>
        {#each $schemataData.contextsStore.filter(c => c.unitId === selectedUnit.unitId) as context}
          <Item
            class="pl-4 justify-center"
            on:SMUI:action={() => (selectedContext = context)}
            selected={selectedContext && selectedContext.contextId === context.contextId}
          >
            <Text>{context.namespace}</Text>
          </Item>
        {/each}
      </SelectionGroup>
    {/if}
  </List>
  <div class="d-flex justify-center pa-4" style="border-top: 1px solid;">
    <Button on:click={() => showSchemataModal = true}>Open Schemata</Button>
  </div>
</Menu>
</div>

<SchemataEmded bind:show={showSchemataModal} />

<style>
  .bl-1 {
    min-height: 100%;
    border-left: 1px solid var(--theme-text-primary);
  }
</style>