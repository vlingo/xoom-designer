<script>
  import { Button } from '../components/ui';
  import MenuSurface from '@smui/menu-surface';
	import List, { Item, Text } from '@smui/list';
  import Dialog, { Content, Actions } from '@smui/dialog';
	import Portal from "svelte-portal/src/Portal.svelte";
  import { schemataSettings } from '../stores';
	import Textfield from '@smui/textfield';

  let menu;
  let showSettings = false;
  let host = $schemataSettings.host;
  let port = $schemataSettings.port;

  function save() {
    $schemataSettings = {
      host,
      port
    }
  }

</script>

<Button on:click={() => menu.setOpen(true)} aria-label="Open Menu" variant="raised">
  Options
</Button>
<MenuSurface bind:this={menu} anchorCorner="BOTTOM_LEFT">
  <List class="demo-list">
    <Item on:SMUI:action={() => showSettings = true}>
      <Text>Set Schemata Access</Text>
    </Item>
  </List>
</MenuSurface>

<Portal target=".s-app">
  <Dialog
    bind:open={showSettings}
    aria-labelledby="simple-title"
    aria-describedby="simple-content"

  >
    <!-- Title cannot contain leading whitespace due to mdc-typography-baseline-top() -->
    <Content class="d-flex flex-column">
      <Textfield
        class="mb-4"
        style="flex: 1;"
        label="Host"
        required
        bind:value={host}
      />
      <Textfield
        style="flex: 1;"
        label="Port"
        required
        bind:value={port}
      />
    </Content>
    <Actions>
      <Button class="vl-btn-error" on:click={() => showSettings = false}>
        Cancel
      </Button>
      <Button on:click={save}>
        Save
      </Button>
    </Actions>
  </Dialog>
</Portal>

<style>
  :global(.vl-btn-error) {
    --mdc-theme-primary: var(--mdc-theme-error)
  }
</style>