<script>
	import { Switch, Radio } from "svelte-materialify/src";
	import CardForm from "../components/CardForm.svelte";
	import { settings } from "../stores";

	const platforms = [
		{
			name: 'JVM',
			options: [
				{
					name: 'Java',
					disabled: false,
				},
				{
					name: 'Kotlin',
					disabled: true
				}
			],
			default: 'Java',
		},
		{
			name: '.NET',
			options: [
				{
					name: 'C#',
					disabled: false
				},
				{
					name: 'F#',
					disabled: true
				}
			],
			default: 'C#'
		}
	]

	let value = $settings.platformSettings && $settings.platformSettings.platform === ".NET";
  let lang = $settings.platformSettings && $settings.platformSettings.lang;
	let options = platforms.reduce((acc, cur) => {
		if ($settings.platformSettings && $settings.platformSettings.platform === cur.name) acc = [...cur.options]
		return acc;
	}, []);

	$: $settings.platformSettings = {
		platform: value ? '.NET' : 'JVM',
		lang
	};

	function updateOptions() {
		const v = value ? '.NET' : 'JVM';
		const platform = platforms.find(p => p.name === v)
		if (platform) {
			options = platform.options
			lang = platform.options.some(o => o.name === lang) ? lang : platform.default 
		}
	}

	$: value, updateOptions()
</script>

<svelte:head>
	<title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Platform" next="context">
  <div class="vl-switch d-flex justify-center mb-8 mt-8">
    <b>JVM</b>
    <!-- <Switch class="mr-4 ml-8" bind:checked={value} inset></Switch>
    <b>.NET</b> -->
  </div>

  <div class="d-flex justify-center mb-8">
    {#each options as option (option)}
      <div class="ml-6 mr-6">
				<Radio bind:group={lang} disabled={option.disabled} value={option.name}>{option.name}</Radio>
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
	}
</style>