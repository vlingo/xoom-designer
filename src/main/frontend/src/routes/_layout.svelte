<script>
	import { mobileStore, theme } from '../stores';
	import { Button, Icon, MaterialApp } from "svelte-materialify/src";
	import AppBar from 'svelte-materialify/src/components/AppBar';
	import Container from 'svelte-materialify/src/components/Grid/Container.svelte';
	import { mdiMenu, mdiWeatherNight, mdiWeatherSunny } from '@mdi/js';
	import SiteNavigation from '../components/SiteNavigation.svelte';
	import { onMount } from 'svelte';

	export let segment;

	let sidenav = false;
	let breakpoints = {};
	let mobile = false;
	$: $mobileStore = mobile;
	function checkMobile() {
		mobile = window.matchMedia(breakpoints['md-and-down']).matches;
	}
	onMount(() => {
		$theme = window.localStorage.getItem('theme') || 'light';
		const unsubscribe = theme.subscribe((value) => {
			window.localStorage.setItem('theme', value);
		});
		import('svelte-materialify/src/utils/breakpoints').then(({ default: data }) => {
			breakpoints = data;
			checkMobile();
		});
		return unsubscribe;
	});

	const toggleTheme = () => $theme = ($theme === "light") ? "dark" : "light";
	$: bgTheme = ($theme === "light") ? "#ffffff" : "#212121"
</script>

<svelte:window on:resize={checkMobile} />

<div style="height: 100%; background-color: {bgTheme}">
<MaterialApp theme={$theme}>
	<AppBar fixed style="width:100%"> <!-- class={'primary-color theme--dark'} -->
    	<div slot="icon">
    	  {#if mobile}
    	    <Button fab depressed on:click={() => (sidenav = !sidenav)} aria-label="Open Menu">
    	    	<Icon path={mdiMenu} />
    	    </Button>
    	  {/if}
		</div>
		<a href="." slot="title" class="text--primary"><span style="color: var(--theme-text-primary);"> VLINGO/XOOM-STARTER </span></a>
		<div style="flex-grow:1" />
    	<!-- <a
    	  href="https://github.com/TheComputerM/svelte-materialify"
    	  target="_blank"
    	  rel="noopener">
    	  <Button class="white-text grey darken-3" aria-label="GitHub" fab={mobile}>
    	    <Icon path={mdiGithub} class={!mobile ? 'mr-3' : ''} />
    	    {#if !mobile}GitHub{/if}
    	  </Button>
    	</a> -->
    	<Button fab text on:click={toggleTheme} aria-label="Toggle Theme">
    		<Icon path={$theme === "light" ? mdiWeatherNight : mdiWeatherSunny}/>
    	</Button>
	</AppBar>

	<SiteNavigation {segment} {mobile} bind:sidenav />

	<main class:navigation-enabled={!mobile}>
		<Container>
    	<!-- {#if ...}
    		<Loading />
    	{/if} -->
		<slot />
		</Container>
  	</main>
</MaterialApp>
</div>

<style>
	main {
	  padding-top: 5rem;
	}
	.navigation-enabled {
	  padding: 5rem 11rem 0 18rem;
	}
</style>