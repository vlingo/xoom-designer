<script>
  import { createEventDispatcher } from "svelte";
  import CreateButton from "./CreateButton.svelte";
  export let title = "";
  export let addable = true;

  const dispatch = createEventDispatcher();
  function add() {
    dispatch('add')
  }
</script>
<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">{title}</h6>
  </legend>
  <slot />
  {#if addable}
    <CreateButton title="Add" on:click={add} />
  {/if}
</fieldset>