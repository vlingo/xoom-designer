<script>
  import Button, { Label, Icon } from '@smui/button';
</script>

<Button {...$$restProps} on:click>
  {#if $$slots.prependIcon}
    <Icon class="material-icons">
      <slot name="prependIcon" />
    </Icon>
  {/if}
  {#if $$slots.default}
    <Label>
      <slot />
    </Label>
  {/if}
  {#if $$slots.appendIcon}
    <Icon class="material-icons">
      <slot name="appendIcon" />
    </Icon>
  {/if}
</Button>