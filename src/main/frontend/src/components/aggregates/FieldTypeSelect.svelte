<script>
  import Select, { Option } from '@smui/select';

  export let disabled;
  export let items;
  export let value;
  export let collectionType;

  let innerValue = value;
  let innerItems = [];

  $: innerItems = collectionType ? items.map(item => ({
    name: `${collectionType}<${item.value}>`,
    value: item.value
  })) : [...items];

  $: innerValue = value;

  function update(e) {
    const ind = e.detail.index;
    if (ind > 0) {
      value = innerItems[ind] && innerItems[ind].value;
    }
  }
</script>

<Select
  on:MDCMenu:selected={(e) => update(e)}
  bind:value={innerValue}
  {disabled}
  required
  label="Type"
>
  {#each innerItems as item (item.name)}
    <Option value={item.value}>{item.name}</Option>
  {/each}
</Select>
