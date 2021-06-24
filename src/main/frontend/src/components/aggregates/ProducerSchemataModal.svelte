<script>
  import { onMount } from "svelte";
  import Dialog, { Title, Content, Header } from '@smui/dialog';
  import IconButton from '@smui/icon-button';
  import Portal from "svelte-portal/src/Portal.svelte";
  import { schemataData } from "../../stores";

  export let show = false;

  let iframe;

  let src = "http://localhost:3001";

  onMount(() => {
    window.addEventListener("message", (event) => {
      if (event.origin !== src) return;

      $schemataData = JSON.parse(event.data)
    }, false);
    return () => {
      window.removeEventListener("message", () => {});
    }
  })
</script>

{#if show}
  <Portal target=".s-app">
    <Dialog
      bind:open={show}
      fullscreen
      class="schemata-dialog"
    >
      <!-- <Header>
        <Title></Title>
        <IconButton action="close" class="material-icons">close</IconButton>
      </Header> -->
      <Content class="schemata-content">
        <iframe
          bind:this={iframe}
          title="XOOM-SCHEMATA"
          {src}
          id="iframe"
          name="iframe"></iframe>
      </Content>
    </Dialog>
  </Portal>
{/if}

<style>
  :global(.schemata-content), #iframe {
    padding: 0 !important;
    margin: 0;
    border: none;
    outline: none;
    border-radius: 20px;
    width: 100%;
    height: 100%;
  }
  :global(.schemata-content) {
    overflow: hidden;
  }
  :global(.schemata-dialog .mdc-dialog__surface) {
    max-width: calc(100vw - 5rem);
    max-height: calc(100vh - 110px);
    border-radius: 20px;
    overflow: hidden;
    background: transparent;
    width: 100%;
    height: 100%;
  }
  :global(.schemata-dialog .mdc-dialog__container) {
    width: 100%;
  }
</style>