<script>
  import { onMount } from "svelte";
  import Dialog, { Title, Content, Header } from '@smui/dialog';
  import IconButton from '@smui/icon-button';
  import Portal from "svelte-portal/src/Portal.svelte";
  import { schemataData } from "../../stores";

  let iframe;
  export let show = false;
  let origin = 'http://localhost:3001';
  let src = `${origin}/organization?designer=true&producer=true`;

  onMount(() => {
    window.addEventListener("message", (event) => {
      if (event.origin !== origin) return;

      $schemataData = JSON.parse(event.data)
    }, false);
    return () => {
      window.removeEventListener("message", () => {});
    }
  })
</script>

<Portal target=".s-app">
  <Dialog
    bind:open={show}
    fullscreen
    class="schemata-dialog"
  >
    <Header>
      <Title></Title>
      <IconButton action="close" class="material-icons">close</IconButton>
    </Header>
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
    max-height: calc(100vh - 110px);
    border-radius: 20px;
    overflow: hidden;
    width: 100%;
    height: 100%;
  }
  :global(.schemata-dialog .mdc-dialog__container) {
    width: 100%;
  }
  :global(.theme--dark .mdc-dialog__surface) {
    --mdc-theme-surface: var(--theme-surface);
    --mdc-theme-on-surface: var(--theme-text-primary);
  }
</style>