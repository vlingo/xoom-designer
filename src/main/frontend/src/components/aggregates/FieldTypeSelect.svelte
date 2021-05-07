<script>
  import { Select } from 'svelte-materialify/src';

  export let mandatory;
  export let disabled;
  export let items;
  export let value;
  export let collectionType;

  let innerValue = value;
  let innerItems = [];

  $: innerItems = collectionType && !collectionType instanceof Object ? items.map(item => ({
    name: `${collectionType}<${item.value}>`,
    value: item.value
  })) : [...items];

  function update(e) {
    value = e.detail;
  }
</script>

<Select {mandatory} {disabled} items={innerItems} bind:value={innerValue} on:change={(e) => update(e)}>
  <slot />
</Select>
