<script>
	import { Switch, Radio } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { platformSettings, setLocalStorage } from "../stores";

	let value = $platformSettings && $platformSettings.platform === ".NET";
  let lang = $platformSettings && $platformSettings.lang;

	const DotNetOptions = ['C#', 'F#'];
	const JvmOptions = ['Java', 'Kotlin'];

	$: $platformSettings = {
		platform: value ? '.NET' : 'JVM',
		lang
	};
	$: setLocalStorage("platformSettings", $platformSettings);
	$: options = value ? DotNetOptions : JvmOptions;
	$: valid = ($platformSettings.platform === ".NET" && DotNetOptions.includes($platformSettings.lang)) || ($platformSettings.platform === 'JVM' && JvmOptions.includes($platformSettings.lang))
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Platform" next="context" bind:valid>
  <div class="vl-switch d-flex justify-center mb-8 mt-8">
    <b>JVM</b>
    <Switch class="mr-4 ml-8" bind:checked={value} inset></Switch>
    <b>.NET</b>
  </div>

  <div class="d-flex justify-center mb-8">
    {#each options as option (option)}
      <div class="ml-6 mr-6">
				<Radio bind:group={lang} value={option}>{option}</Radio>
			</div>
    {/each}
  </div>
</CardForm>

<style global lang="scss">
	.vl-switch {
		.s-switch, .s-switch__wrapper.inset, .s-switch__wrapper.inset .s-switch__track {
			width: 100px;
		}
		.s-switch__wrapper>input:checked~.s-switch__thumb {
			transform: translate(72px);
		}

		.s-switch__wrapper.primary-text {
			color: rgba(255, 255, 255, 0.3) !important;
    	caret-color: rgba(255, 255, 255, 0.3) !important;
		}
	}
</style>