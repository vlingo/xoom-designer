<script>
  import Tooltip, { Wrapper } from '@smui/tooltip';
  import IconButton from '@smui/icon-button';

  export let type = 'error';
  export let messages;
  export let names;
  $: internalMessages = messages.reduce((acc, cur, index) => {
    if (cur) {
      acc.push(`${names[index]} ${cur}`)
    }
    return acc;
  }, []);
</script>

{#if internalMessages.length > 0}
<Wrapper>
  <IconButton class="material-icons-round {type === 'error' ? 'vl-error-t' : 'vl-warning-t'}" ripple={false}>warning</IconButton>
  <Tooltip xPos="end" yPos="above" class={type === 'error' ? 'vl-error-t' : 'vl-warning-t'}>
    <div style="text-align: left;">
      {#each internalMessages as message}
        {#if message}
          <div>{message}</div>
        {/if}
      {/each}
    </div>
  </Tooltip>
</Wrapper>
{/if}
<style>
  :global(.mdc-tooltip) {
    z-index: 9999999 !important;
	}
  :global(.vl-error-t) {
    color: red !important;
    line-height: 0.6;
  }
  :global(.vl-warning-t) {
    color: orange !important;
    line-height: 0.6;
  }
  :global(.mdc-tooltip__surface) {
    background-color: white !important;
    border: 1px solid rgba(0, 0, 0, 0.2);
    color: inherit !important;
  }
</style>