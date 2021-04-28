<script>
	export let mobile;
	export let sidenav;
	import { NavigationDrawer, Overlay, Icon } from 'svelte-materialify/src';
	import routes from '../util/routes';
	import List, {
    Item,
    Text,
  } from '@smui/list';
	import { goto } from '@sapper/app';

	export let segment;
</script>
  
<NavigationDrawer active={!mobile || sidenav} style="height:100vh;" fixed clipped borderless>
	<List nav dense>
		{#each routes as item}
			<Item
				on:SMUI:action={() => goto(item.href)}
				selected={segment === item.href}
			>
				<Icon path={segment === item.href ? item.openIcon : item.icon} />
				<Text class="ml-3">{item.text}</Text>
			</Item>
		{/each}
	</List>
</NavigationDrawer>
<Overlay index="3" active={mobile && sidenav} on:click={() => (sidenav = false)} fadeOptions={{ duration: 250 }} />