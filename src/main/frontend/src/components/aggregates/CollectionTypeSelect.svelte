<script>
  import { collectionTypes } from "../../stores";
  import List, { Item, Label } from '@smui/list';
  import Checkbox from '@smui/checkbox';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';
  import Textfield from "@smui/textfield/Textfield.svelte";


  export let value;
  export let disabled;

  let innerValue = value;

  let menu;
  let anchor;
  let anchorClasses = {};


  function selected(name) {
    if (name === value) {
      value = null
    } else {
      value = name;
    }
  }

  $: innerValue = value;
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
  <div on:click={() => menu.setOpen(true)}>
    <Textfield
      style="width: 100%;"
      value={value ? innerValue : '(bare)'}
      {disabled}
      label="Collection"
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
          {#each collectionTypes as ct}
            <Item
              class="pa-0"
              on:SMUI:action={() => selected(ct.name)}
              selected={value === ct.name}
            >
              <Checkbox
                checked={value === ct.name}
                value={ct.name} />
              <Label>{ct.name}</Label>
            </Item>
          {/each}
        </SelectionGroup>
      </List>
    </Menu>
</div>
