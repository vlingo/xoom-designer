<script>
	import { onMount } from 'svelte';
	import Base64 from "../util/Base64";
	import MenuSurface from '@smui/menu-surface';
	import { Button, Icon, MaterialApp, Divider, AppBar, Container, Dialog, Card, CardTitle, CardActions, CardText, Snackbar } from "svelte-materialify/src";
	import { theme, isMobile, settingsInfo, projectGenerationIndex, generatedProjectsPaths, settings, clearSettings, isSettingsEdited, updateSettings } from '../stores';
	import { mdiCheckBold, mdiCloseThick, mdiMenu, mdiWeatherNight, mdiWeatherSunny } from '@mdi/js';
	import Portal from "svelte-portal/src/Portal.svelte";
	import List, { Item, Text } from '@smui/list';
	import SiteNavigation from '../components/SiteNavigation.svelte';
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
	import DownloadDialog from "../util/DownloadDialog";
	import Formatter from "../util/Formatter";
	export let segment;
	import { goto } from '@sapper/app';
	import logo from '../images/xoom-horizontal_designer.png';

	let sidenav = false;
	let snackbar = false;
	let importDialogActive = false;
	let settingsResetDialog = false;
	let menu, succeded, failed, successMessage, failureMessage;

	const toggleTheme = () => $theme = ($theme === "light") ? "dark" : "light";
	$: bgTheme = ($theme === "light") ? "#ffffff" : "#212121";

	function openMenu() {
		menu.setOpen(true);
	}

	function openSettingsImportationDialog() {
		if(isSettingsEdited()) {
			importDialogActive = true;
		} else {
			openFileExplorer();
		}
	}

	function handleUpload() {
		if(this.files.length == 0) {
			return;
		}
		Base64.encode(this.files[0]).then(encodedFile => {
			XoomDesignerRepository.processImportFile(encodedFile)
			.then(importedSettings => {
				updateSettings(importedSettings);
				succeed("Settings imported.");
			}).catch(() => {
				fail(["Settings importation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"]);
			}).finally(() => {
				snackbar = true;
				this.form.reset();
			})
		});
	}

	function exportSettings() {
		XoomDesignerRepository.downloadExportationFile($settings)
			.then(settingsFile => {
					succeed("Settings exported.");
					DownloadDialog.forJsonFile(Formatter.buildSettingsFullname($settings.context), settingsFile.encoded);
				}).catch(() => {
					failed(["Settings exportation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"]);
				}).finally(() => {
					snackbar = true;
				})
	}

	function openSettingsResetDialog() {
		if(isSettingsEdited()) {
			settingsResetDialog = true;
		} else {
			resetSettings();
		}
	}

	function closeResetDialog() {
		settingsResetDialog = false;
	}

	function resetSettings() {
		succeed("Settings reset.");
		closeResetDialog();
		clearSettings();
		snackbar = true;
		goto('/context');
	}

	function succeed(message) {
		successMessage = message;
		snackbar = true;
		succeded = true;
	}

	function handleModelProcessFailure(failureName) {
		failureDialogTitle = failureName;
		failureDialogActive = true;
		errorDetailsCopied = false;
    	succeded = false;
		snackbar = false;
	}

	function openFileExplorer() {
		document.getElementById("fileImport").click();
		closeImportDialog();
	}

	function closeImportDialog() {
		importDialogActive = false;
	}

	onMount(() => {
		isMobile.check();
		XoomDesignerRepository.queryInfo()
			.then(data => {
				if ($settingsInfo && $settingsInfo.xoomDesignerFileVersion && data && data.xoomDesignerFileVersion && $settingsInfo.xoomDesignerFileVersion !== data.xoomDesignerFileVersion) {
					const tempProjectGenerationIndex = Number($projectGenerationIndex);
					const tempGeneratedProjectsPaths = [...$generatedProjectsPaths];
					const tempSettingsInfo = {...$settingsInfo};
					localStorage.clear();
					$projectGenerationIndex = Number(tempProjectGenerationIndex);
					$generatedProjectsPaths = tempGeneratedProjectsPaths;
					$settingsInfo = tempSettingsInfo;
				}
				$settingsInfo = data;			
			});
	})

</script>

<svelte:window on:resize={isMobile.check} />

<div style="height: 100%; background-color: {bgTheme}">
<MaterialApp theme={$theme}>
	<AppBar fixed style="width:100%"> <!-- class={'primary-color theme--dark'} -->
    	<div slot="icon">
    	  {#if $isMobile}
    	    <Button fab depressed on:click={() => (sidenav = !sidenav)} aria-label="Open Menu">
    	    	<Icon path={mdiMenu} />
    	    </Button>
    	  {/if}
		</div>
		<a href="/platform" slot="title" class="text--primary d-flex">
			<img
				style="width: 256px;"
				src={logo}
				alt="VLINGO XOOM Designer"
			/>
		</a>
		<Divider vertical inset class="ml-4 mr-4" />

		<div style="min-width: 100px;">
			<Button on:click={openMenu} aria-label="Open Menu">
				<Text class="ml-2">Model</Text>
			</Button>
			<MenuSurface bind:this={menu} anchorCorner="BOTTOM_LEFT">
				<List class="demo-list">
					<Item on:SMUI:action={exportSettings}>
						<Text>Export</Text>
					</Item>
					<Item on:SMUI:action={openSettingsImportationDialog}>
						<Text>Import</Text>
					</Item>
					<Item on:SMUI:action={openSettingsResetDialog}>
						<Text>Reset</Text>
					</Item>
				</List>
			</MenuSurface>
		</div>

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

	<form><input id="fileImport" type="file" hidden={true} on:change={handleUpload} accept="application/json"></form>

	<Snackbar class="justify-space-between" bind:active={snackbar} top right>
		{#if succeded}
			<Icon class="green-text" path={mdiCheckBold}/> {successMessage}
		{:else if failed}
			<Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
		{/if}
		<Button text on:click={() => snackbar = false}>
			Dismiss
		</Button>
	</Snackbar>

	<Portal target=".s-app">
		<Dialog persistent bind:active={importDialogActive}>
			<Card class="pa-3">
				<div class="d-flex flex-column">
					<CardTitle class="error-text">
						Be Careful!
					</CardTitle>
					<CardText>
						This action will overwrite the fields already filled in.
					</CardText>
					<CardActions style="margin-top: auto" class="justify-space-around">
						<Button on:click={closeImportDialog}>Cancel</Button>
						<Button class="primary-color" on:click={openFileExplorer}>Continue</Button>
					</CardActions>
				</div>
			</Card>
		</Dialog>
		<Dialog persistent bind:active={settingsResetDialog}>
			<Card class="pa-3">
				<div class="d-flex flex-column">
					<CardTitle class="error-text">
						Be Careful!
					</CardTitle>
					<CardText>
						This will clear your browser's storage of the current XOOM Designer model. You can download and save your model definition first for back up. Are you sure?
					</CardText>
					<CardActions style="margin-top: auto" class="justify-space-around">
						<Button on:click={closeResetDialog}>Cancel</Button>
						<Button class="primary-color" on:click={resetSettings}>Continue</Button>
					</CardActions>
				</div>
			</Card>
		</Dialog>
	</Portal>
			
	<SiteNavigation {segment} bind:mobile={$isMobile} bind:sidenav />
	<main class:navigation-enabled={!$isMobile}>
		<section align="end">
			{#if failed}
				<Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
				<a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failureMessage[1]}</a>
			{/if}
		</section>

		<Container>
    	<!-- {#if ...}
    		<Loading />
    	{/if} -->
			{#if $settings}
				<slot />
			{/if}
		</Container>

  	</main>
		
	<div id="portal"></div>

</MaterialApp>
</div>

<style lang="scss" global>
	main {
	  padding-top: 5rem;
	}
	.navigation-enabled {
	  padding: 5rem 5rem 5rem calc(256px + 5rem);
	}

	.error-text {
		.s-input__details {
			color: inherit;
		}
	}
	.theme--dark {
		.mdc-text-field *, .mdc-select *, .mdc-text-field-helper-line, .mdc-menu * {
			color: white !important;
		}
		.mdc-select__menu, .mdc-menu {
			background-color: #212121;
		}
	}
</style>
