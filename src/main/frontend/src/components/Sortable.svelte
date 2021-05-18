<script>
  import { onMount } from "svelte";
  import Sortable from 'sortablejs';

  export let options = {};
  let sortable;

  onMount(() => {
		Sortable.create(sortable, {
      filter: '.disabled',
      onMove: event => {
        return !event.related.classList.contains('disabled');
      },
      handle: '.handle',
			animation: 100,
      ...options
		});
	});
</script>

<div bind:this={sortable}>
  <slot />
</div>