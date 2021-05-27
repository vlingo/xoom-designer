<script>
  import Fab, { Icon } from '@smui/fab';

	export let title;
</script>



<div class="vl-btn d-flex justify-center align-center">
	<Fab {title} color="primary" on:click>
		<Icon class="material-icons">add</Icon>
	</Fab>
</div>

<style>
	:global(fieldset) { 
		position: relative;
	}
	.vl-btn {
		position: absolute;
    bottom: -14px;
    right: -14px;
    display: flex;
    width: auto;
    height: auto;
	}
</style>