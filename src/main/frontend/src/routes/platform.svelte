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
					value: 'Java',
					disabled: false,
          sdkVersions: [
            {
              name: "JDK 8",
              value: "1.8"
            },
            {
              name: "JDK 10",
              value: "10"
            }
          ],
           vlingoVersions: [
             {
               name: "1.9.3 (Stable)",
               value: "1.9.3"
             }
           ]
				},
				{
					name: 'Kotlin',
					value: 'Kotlin',
					disabled: true,
          sdkVersions: [],
          vlingoVersions: []
				}
			],
			default: 'Java',
		},
		{
			name: '.NET',
			options: [
				{
					name: 'C#',
					value: 'C_SHARP',
					disabled: false,
					sdkVersions: [
						{
							name: "Net 6.0",
							value: "net6.0"
						}
					],
          vlingoVersions: [
            {
              name: "1.9.3 (Stable)",
              value: "1.9.3"
            }
          ]
				},
				{
					name: 'F#',
					value: 'F_SHARP',
					disabled: true,
          sdkVersions: [],
          vlingoVersions: []
				}
			],
			default: 'C#'
		}
	]

	let value = $settings.platformSettings && $settings.platformSettings.platform === ".NET";
  let lang = $settings.platformSettings && $settings.platformSettings.lang;
  let sdkVersion = $settings.platformSettings && $settings.platformSettings.sdkVersion;
  let vlingoVersion = $settings.platformSettings && $settings.platformSettings.vlingoVersion;
	let options = platforms.reduce((acc, cur) => {
		if ($settings.platformSettings && $settings.platformSettings.platform === cur.name) acc = [...cur.options]
		return acc;
	}, []);

	$: $settings.platformSettings = {
		platform: value ? '.NET' : 'JVM',
		lang,
		sdkVersion,
		vlingoVersion
	};

	function updateOptions() {
		const v = value ? '.NET' : 'JVM';
		const platform = platforms.find(p => p.name === v)
		if (platform) {
			options = platform.options
			lang = platform.options.some(o => o.value === lang) ? lang : platform.default
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
    <Switch class="mr-4 ml-8" bind:checked={value} inset></Switch>
    <b>.NET</b>
  </div>

  <div class="d-flex justify-center mb-8">
    <fieldset class="mb-8">
  	<legend>Language: </legend>
    {#each options as option (option)}
      <div class="ml-6 mr-6">
				<Radio bind:group={lang} disabled={option.disabled} value={option.value}>{option.name}</Radio>
			</div>
    {#if !option.disabled}
    <fieldset>
  	<legend>SDK Version: </legend>
		{#each option.sdkVersions as sdk (sdk)}
      <div class="ml-6 mr-6">
        <Radio bind:group={sdkVersion} value={sdk.value}>{sdk.name}</Radio> <br />
      </div>
    {/each}
    </fieldset>
    <fieldset>
  	<legend>VLINGO Version: </legend>
		{#each option.vlingoVersions as vlingo (vlingo)}
      <div class="ml-6 mr-6">
        <Radio bind:group={vlingoVersion} value={vlingo.value}>{vlingo.name}</Radio> <br />
      </div>
    {/each}
    </fieldset>
    {/if}
    {/each}
    </fieldset>
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