<script>
  import IconButton from '@smui/icon-button';
  import { schemataData } from "../../stores";
  import Textfield from '@smui/textfield/Textfield.svelte';
  import Button from '@smui/button';

  import Menu, { SelectionGroup } from '@smui/menu';
  import List, { Item, Text } from '@smui/list';
  import { Anchor } from '@smui/menu-surface';

  import SchemataEmded from './SchemataEmded.svelte';

  let anchor;
  let anchorClasses = {};
  let menu;
  let showSchemataModal = false;

  export let schema;
  export let invalid;

  let schemaParsed = schema && schema.split(':');
  let selectedOrg = schemaParsed ? $schemataData.organizationsStore.find(org => org.name === schemaParsed[0]) : null;
  let selectedUnit = schemaParsed ? $schemataData.unitsStore.find(unit => unit.name === schemaParsed[1]) : null;
  let selectedContext = schemaParsed ? $schemataData.contextsStore.find(context => context.namespace === schemaParsed[2]) : null;
  let selectedSchema = schemaParsed ? $schemataData.schemasStore.find(schema => schema.namespace === schemaParsed[3]) : null;
  let selectedSchemaVersion = schemaParsed ? $schemataData.schemaVersionsStore.find(sv => sv.namespace === schemaParsed[4]) : null;

$: schema = `${selectedOrg ? selectedOrg.name : ''}${selectedUnit ? `:${selectedUnit.name}` : ''}${selectedContext ? `:${selectedContext.namespace}` : ''}${selectedSchema ? `:${selectedSchema.name}` : ''}${selectedSchemaVersion ? `:${selectedSchemaVersion.currentVersion}` : ''}`;
$: if(!selectedOrg) selectedUnit = null;
$: if(!selectedUnit) selectedContext = null;
$: if(!selectedContext) selectedSchema = null;
$: if(!selectedSchema) selectedSchemaVersion = null;

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
    label="Organization : Unit : Context : Schema : Schema Version"
    value={schema}
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
                selectedSchema = null;
                selectedSchemaVersion = null;
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
                  selectedSchema = null;
                  selectedSchemaVersion = null;
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
              on:SMUI:action={() => {
                if (!selectedContext || selectedContext.contextId !== context.contextId) {
                  selectedContext = context;
                  selectedSchema = null;
                  selectedSchemaVersion = null;
                }
              }}
              selected={selectedContext && selectedContext.contextId === context.contextId}
            >
              <Text>{context.namespace}</Text>
            </Item>
          {/each}
        </SelectionGroup>
      {/if}
      {#if selectedContext}
        <div class="bl-1"></div>
        <SelectionGroup style="flex: 1;">
          <Item class="pl-4 justify-center" disabled={true}>
            <Text>Schema</Text>
          </Item>
          {#each $schemataData.schemasStore.filter(sc => sc.contextId === selectedContext.contextId) as schema}
            <Item
              class="pl-4 justify-center"
              on:SMUI:action={() => {
                if (!selectedSchema || selectedSchema.schemaId !== schema.schemaId) {
                  selectedSchema = schema
                  selectedSchemaVersion = null;
                }
              }}
              selected={selectedSchema && selectedSchema.schemaId === schema.schemaId}
            >
              <Text>{schema.name}</Text>
            </Item>
          {/each}
        </SelectionGroup>
      {/if}
      {#if selectedSchema}
        <div class="bl-1"></div>
        <SelectionGroup style="flex: 1;">
          <Item class="pl-4 justify-center" disabled={true}>
            <Text>Shema Version</Text>
          </Item>
          {#each $schemataData.schemaVersionsStore.filter(scv => scv.schemaId === selectedSchema.schemaId) as schemaVersion}
            <Item
              class="pl-4 justify-center"
              on:SMUI:action={() => (selectedSchemaVersion = schemaVersion)}
              selected={selectedSchemaVersion && selectedSchemaVersion.schemaVersionId === schemaVersion.schemaVersionId}
            >
              <Text>{schemaVersion.currentVersion}</Text>
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

<SchemataEmded bind:show={showSchemataModal} limitless={true} />