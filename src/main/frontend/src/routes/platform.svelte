<script>
  import { Switch, Radio } from "svelte-materialify/src";
  import CardForm from "../components/CardForm.svelte";
  import { settings } from "../stores";

  const platforms = [
    {
      name: "JVM",
      options: [
        {
          name: "Java",
          value: "Java",
          disabled: false,
          sdkVersions: [],
          xoomVersions: [],
        },
        {
          name: "Kotlin",
          value: "Kotlin",
          disabled: true,
          sdkVersions: [],
          xoomVersions: [],
        },
      ],
      default: "Java",
    },
    {
      name: ".NET",
      options: [
        {
          name: "C#",
          value: "C_SHARP",
          disabled: false,
          sdkVersions: [
            {
              name: "Net 6.0",
              value: "net6.0",
            },
          ],
          xoomVersions: [
            {
              name: "1.9.3 (Stable)",
              value: "1.9.3",
            },
          ],
        },
        {
          name: "F#",
          value: "F_SHARP",
          disabled: true,
          sdkVersions: [],
          xoomVersions: [],
        },
      ],
      default: "C#",
    },
  ];

  let value = $settings.platformSettings && $settings.platformSettings.platform === ".NET";
  let platform = !$settings.platformSettings ? "JVM" : $settings.platformSettings.platform;
  let lang = !$settings.platformSettings ? "Java" : $settings.platformSettings.lang;
  let sdkVersion = !$settings.platformSettings ? "" : $settings.platformSettings.sdkVersion;
  let xoomVersion = !$settings.platformSettings ? "" : $settings.platformSettings.xoomVersion;
  let options = platforms.reduce((acc, cur) => {
    if ( $settings.platformSettings &&  $settings.platformSettings.platform === cur.name)
      acc = [...cur.options];
    return acc;
  }, []);

  function updateOptions() {
    const v = value ? ".NET" : "JVM";
    const selectedPlatform = platforms.find((p) => p.name === v);
    if (selectedPlatform) {
      options = selectedPlatform.options;
      lang = selectedPlatform.options.some((o) => o.value === lang) ? lang : selectedPlatform.default;
      platform = selectedPlatform.name;
    }
    $settings.platformSettings = {
      platform,
      lang,
      sdkVersion,
      xoomVersion,
    };
  }

  $: value || lang, updateOptions();
</script>

<svelte:head>
  <title>Context</title>
</svelte:head>

<!-- add newbie tooltips -->
<CardForm title="Platform" next="context">
  <div class="vl-switch d-flex justify-center mb-8 mt-8">
    <b>JVM</b>
    <Switch class="mr-4 ml-8" bind:checked={value} inset />
    <b>.NET</b>
  </div>

  <div class="d-flex justify-center mb-8">
    <fieldset style="width: 50%;padding: 15px; border: none">
      <legend>Language: </legend>
      <div class="d-flex justify-center">
        {#each options as option (option)}
          <div class="ml-6 mr-6">
            <Radio bind:group={lang} disabled={option.disabled} value={option.value}>{option.name}</Radio>
          </div>
        {/each}
      </div>
      {#each options as option (option)}
        {#if !option.disabled}
          {#if option.sdkVersions.length > 0}
            <fieldset style="border: none">
              <legend>SDK Version: </legend>
              <div class="d-flex">
                {#each option.sdkVersions as sdk (sdk)}
                  <div class="ml-6 mr-6">
                    <Radio bind:group={sdkVersion} value={sdk.value}>{sdk.name}</Radio>
                  </div>
                {/each}
              </div>
            </fieldset>
          {/if}
          {#if option.xoomVersions.length > 0}
            <fieldset style="border: none">
              <legend>VLINGO Version: </legend>
              <div class="d-flex">
                {#each option.xoomVersions as vlingo (vlingo)}
                  <div class="ml-6 mr-6">
                    <Radio bind:group={xoomVersion} value={vlingo.value}>{vlingo.name}</Radio>
                  </div>
                {/each}
              </div>
            </fieldset>
          {/if}
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
