<script>
	import { Select } from "svelte-materialify/src";
  import { createEventDispatcher } from 'svelte';

  const dispatch = createEventDispatcher();

  export let value;
  let previousValue = value;

  $: if (previousValue !== value) {
    previousValue = value;
    dispatch('change', value);
  }
</script>

<Select {...$$props} bind:value>Storage Type</Select>

