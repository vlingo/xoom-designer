<script>
  import {onMount} from "svelte";
  import Dialog, {Content, Header, Title} from '@smui/dialog';
  import IconButton from '@smui/icon-button';
  import Portal from "svelte-portal/src/Portal.svelte";
  import {schemataData, settings} from "../../stores";

  export let show = false;
  export let reload = false;
  export let limitless = false;

  let iframe;
  let origin, src;

  onMount(() => {
    clearSchemataDataWhenSchemataNotAvailable();
    window.addEventListener("message", (event) => {
      if (event.origin !== origin) return;

      $schemataData = JSON.parse(event.data);
    }, false);
    return () => {
      window.removeEventListener("message", () => {});
    }
  })

  const reloadIFrame = () => {
    document.querySelector('iframe').remove();
    const newIframe = document.createElement('iframe');
    newIframe.src = src;
    document.getElementsByClassName("schemata-content")[0].appendChild(newIframe);
  }

  const clearSchemataDataWhenSchemataNotAvailable = () => {
    fetch(src, {mode: "no-cors"}).catch(error => {
      $schemataData = {
        organizationsStore: [],
        unitsStore: [],
        contextsStore: [],
        schemasStore: [],
        schemaVersionsStore: [],
      }
    })
  }

  $: origin = `http://${$settings.schemata.host}:${$settings.schemata.port}`;
  $: src = `${origin}/organization${limitless ? '' : '#producer=true'}`;
  $: console.log($settings.schemata);
  $: if(reload) {
    reloadIFrame();
  }
</script>

<Portal target=".s-app">
  {#key src}
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
  {/key}

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
</style>