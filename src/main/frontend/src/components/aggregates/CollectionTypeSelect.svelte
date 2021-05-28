<script>
  import { collectionTypes } from "../../stores";
  import Select, { Option } from '@smui/select';

  export let value;
  export let disabled;

  let innerValue = value;

  function selected(e) {
    const selectedOption = collectionTypes[e.detail.index];
    if (selectedOption.name === value) {
      value = null
    } else {
      value = selectedOption.name;
    }
  }

  $: innerValue = value;
</script>

<Select
  {disabled}
  bind:value={innerValue}
  label="Collection{value ? '' : ' (bare)'}"
  on:MDCMenu:selected={(e) => selected(e)}
>
  {#each collectionTypes as type}
    <Option value={type.name}>{type.name}</Option>
  {/each}
</Select>