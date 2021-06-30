<script>
  import { TARGET } from "../stores/generation"
	import CardForm from "../components/CardForm.svelte";
	import { settings, isSettingsComplete, settingsInfo, projectGenerationIndex, generatedProjectsPaths } from "../stores";
	import XoomDesignerRepository from "../api/XoomDesignerRepository";
  import DownloadDialog from "../util/DownloadDialog";
  import Formatter from "../util/Formatter";
	import { requireRule } from "../validators";
  import { mdiCheckBold, mdiCloseThick } from "@mdi/js";
  import {
    Button,
    Switch,
    ProgressCircular,
    Snackbar,
    Icon,
    Dialog,
    Card,
    CardTitle,
    CardActions,
    CardText,
  } from 'svelte-materialify/src';
  import Portal from "svelte-portal/src/Portal.svelte";

  import List, { Item, Label } from '@smui/list';
  import Menu, { SelectionGroup } from '@smui/menu';
  import { Anchor } from '@smui/menu-surface';
  import Textfield from '@smui/textfield';

  let snackbar = false;
  let isLoading = false;
  let processing = false;
  let dialogActive = false;
  let generateButtonLabel = requiresCompression() ? "Download Project" : "Generate";
  let dialogStatus, succeded, failed, successMessage, failureMessage;
  let menu;
  let anchor;
  let anchorClasses = {}

  function checkPath() {
    if(requiresCompression()){
      generate();
    } else {
      isLoading = true;
      XoomDesignerRepository.checkPath($settings.projectDirectory)
        .then(response => {
          if(response.status === 201) {
            generate();
          } else if (response.status === 403) {
            dialogStatus = 'Forbidden';
            dialogActive = true;
          } else if (response.status === 409) {
            dialogStatus = 'Conflict';
            dialogActive = true;
          }
        })
        .finally(() => {
          isLoading = false;
        })
    }
  }

	const generate = () => {
    if(!valid) return;
    processing = true;
    dialogActive = false;
		XoomDesignerRepository.generateProject($settings)
		  .then(generationReport => {
        if(requiresCompression()) {
          succeed(["Project generated. ", ""]);
          DownloadDialog.forZipFile(Formatter.buildSettingsFullname($settings.context), generationReport.compressedProject);
        } else {
          succeed(["Project generated. ","Please check folder: " + $settings.projectDirectory]);
          $projectGenerationIndex = Number($projectGenerationIndex) + 1;
          $generatedProjectsPaths = [...$generatedProjectsPaths, $settings.projectDirectory];
        }        
      }).catch(() => {
        fail(["Project generation failed. ","Please contact support: https://github.com/vlingo/xoom-designer/issues"]);
      }).finally(() => {
        processing = false;
        snackbar = true;
      })
	}

  function cancelDialog() {
    dialogActive = false
    isLoading = false;
  }

  function succeed(messages) {
    successMessage = messages;
    succeded = true;
    errorDetails = "";
  }

  function handleValidationFailure() {
    validationFailed = true;
    snackbar = true;
    succeded = false;
  }

  function fail(messages) {
    failureMessage = messages;
    succeded = false;
    failed = true;
  }

  function requiresCompression() {
    return $settingsInfo.generationTargetKey === TARGET.ZIP_DOWNLOAD;
  }

  function buildProjectDirectory() {
    let context = $settings.context;
    if(context) {
      return `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}${context.groupId}${$settingsInfo.pathSeparator}${context.artifactId}${Number($projectGenerationIndex)}`;
    }
    return `${$settingsInfo.userHomePath}${$settingsInfo.pathSeparator}VLINGO-XOOM${$settingsInfo.pathSeparator}`;
  }

  $: $settings.projectDirectory = buildProjectDirectory();
	$: if(!$settings.useAnnotations) $settings.useAutoDispatch = false;
  $: valid = (requiresCompression() || $settings.projectDirectory) && isSettingsComplete($settings);
  $: $settings.generateUI, $settings.generateUIWith = 'ReactJS';
</script>

<svelte:head>
	<title>Generation</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Generation" previous="deployment">
  {#if !requiresCompression()}
  <Textfield
    class="mb-4"
    style="width: 100%;"
    input$placeholder={$settingsInfo.userHomePath}
    bind:value={$settings.projectDirectory}
    label="Absolute path where you want to generate the project"
    invalid={[requireRule($settings.projectDirectory)].some(f => f)}
  ></Textfield>
  {/if}
  <div>
    <Switch class="mb-4" bind:checked={$settings.generateUI}>Generate Web UI?</Switch>
    {#if $settings.generateUI}
      <div
        class={Object.keys(anchorClasses).join(' ')}
        use:Anchor={{
          addClass: (className) => {
            if (!anchorClasses[className]) {
              anchorClasses[className] = true;
            }
          },
          removeClass: (className) => {
            if (anchorClasses[className]) {
              delete anchorClasses[className];
              anchorClasses = anchorClasses;
            }
          },
        }}
        bind:this={anchor}
      >
      <div on:click={() => menu.setOpen(true)}>
        <Textfield
          class="mb-4"
          style="width: 100%;"
          value={$settings.generateUIWith}
          label="Generate Web UI with"
          input$readonly={true}
          on:keypress={(e) => {if(e.keyCode === 13 || e.key === 'Enter') menu.setOpen(true)}}
        ></Textfield>
        </div>
        <Menu
          bind:this={menu}
          anchor={false}
          bind:anchorElement={anchor}
          anchorCorner="BOTTOM_LEFT"  
          style="width: 100%;"
        >
          <List class="demo-list" checkList>
            <SelectionGroup>
              <Item
                on:SMUI:action={() => $settings.generateUIWith = 'ReactJS'}
                selected={$settings.generateUIWith === 'ReactJS'}
              >
                <Label>ReactJS</Label>
              </Item>
              <Item
                on:SMUI:action={() => $settings.generateUIWith = 'VueJS'}
                selected={$settings.generateUIWith === 'VueJS'}
                disabled={true}
              >
                <Label>VueJS (coming soon)</Label>
              </Item>
              <Item
                on:SMUI:action={() => $settings.generateUIWith = 'Svelte'}
                selected={$settings.generateUIWith === 'Svelte'}
                disabled={true}
              >
                <Label>Svelte (coming soon)</Label>
              </Item>
            </SelectionGroup>
          </List>
        </Menu>
      </div>
    {/if}
  </div>
	<Switch class="mb-4" bind:checked={$settings.useAnnotations}>Use VLINGO XOOM annotations</Switch>
  <Switch class="mb-4" bind:checked={$settings.useAutoDispatch} disabled={!$settings.useAnnotations}>Use VLINGO XOOM auto dispatch</Switch>
  <Button class="mt-4 mr-4" on:click={checkPath} disabled={!valid || processing || isLoading}>{generateButtonLabel}</Button>
  {#if processing}
    <ProgressCircular indeterminate color="primary" />
  {:else if succeded}
    <Icon class="green-text" path={mdiCheckBold}/> {successMessage[0]+successMessage[1]}
  {:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
    <a href="https://github.com/vlingo/xoom-designer/issues" rel="noopener" target="_blank">{failureMessage[1]}</a>
  {/if}
</CardForm>


<Snackbar class="justify-space-between" bind:active={snackbar} top right>
  {#if succeded}
    <Icon class="green-text" path={mdiCheckBold}/> {successMessage[0]}
  {:else if failed}
    <Icon class="red-text" path={mdiCloseThick}/> {failureMessage[0]}
  {/if}
  <Button text on:click={() => snackbar = false}>
    Dismiss
  </Button>
</Snackbar>

<Portal target=".s-app">
	<Dialog persistent bind:active={dialogActive}>
		<Card class="pa-3">
			<div class="d-flex flex-column">
				<CardTitle class="error-text">
          Be Careful!
        </CardTitle>
        {#if dialogStatus === 'Conflict'}
          <CardText>
            You already generated a project with the same path. If that project still exists and you continue, that project will be overwritten.
          </CardText>
        {:else if dialogStatus === 'Forbidden'}
          <CardText>
            Warning: the directory cannot be used for generation!
          </CardText>
        {/if}
				<CardActions style="margin-top: auto" class="justify-space-around">
          <Button on:click={cancelDialog}>{dialogStatus === 'Forbidden' ? 'OK' : 'Cancel'}</Button>
          {#if dialogStatus === 'Conflict'}
            <Button class="primary-color" disabled={!valid || processing} on:click={generate}>Generate</Button>
          {/if}
				</CardActions>
			</div>
		</Card>
	</Dialog>
</Portal>
