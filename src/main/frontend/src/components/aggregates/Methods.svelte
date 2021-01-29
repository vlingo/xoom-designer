<script>
	import { Select, Switch, TextField } from 'svelte-materialify/src';
  import DeleteButton from "./DeleteButton.svelte";
	import CreateButton from "./CreateButton.svelte";
	import { identifierRule, requireRule } from "../../validators";
  import { formatArrayForSelect } from '../../utils';

  export let methods;
  export let stateFields;
  export let events;

	const addMethod = () => methods = methods.concat({ name: "", useFactory: false, parameters: [], event: "" });
	const deleteMethod = (index) => { methods.splice(index, 1); methods = methods; }
  
  //The items selected in the "event-fields" combo, which are binded in the "events.fields" array, 
  //have to follow the same order of "stateFields" items.
  //the items can be matched by name (eg: "eventField.name == stateField.name").

  /* There was no on:change function emitted from svelte-materialify Select component,
  *  so function would not be called at any moment, instead I used this piece of code to achive what you described in your comment
  */

	$: {
		methods = methods.map((method) => {
			return {
				...method,
				parameters: stateFields.reduce((acc, cur) => {
					if (method.parameters && method.parameters.includes(cur.name)) acc.push(cur.name);
					return acc;
				}, [])
			};
		})
  }

	const isMethodNameUnique = (value) => {
    const result = methods.filter(method => method.name === value);
		return result.length === 1 ? undefined : 'Name must be unique';
  }
  
  $: if (methods) {
    methods.forEach(method => {
      isMethodNameUnique(method.name);
    })
  }
</script>

<h5>Methods:</h5>
{#each methods as method, id}
  <div class="d-flex align-center">
    <div>
      <span class="d-flex">
        <TextField class="ma-2" bind:value={method.name} rules={[requireRule, identifierRule, isMethodNameUnique]} validateOnBlur={!method.name}>Name</TextField>
        <Select id="method-parameter" mandatory disabled={!stateFields.length} multiple class="ma-2" items={formatArrayForSelect(stateFields.map(f => f.name))} bind:value={method.parameters}>Parameters</Select>
        <Select mandatory disabled={!events.length} class="ma-2" items={formatArrayForSelect(events.map(e => e.name))} bind:value={method.event}>Event</Select>
      </span>
      <div>
        <Switch bind:checked={method.useFactory}>Involves creation of entity?</Switch>
      </div>
    </div>
    <DeleteButton title="Delete Method" on:click={() => deleteMethod(id)}/>
  </div>
{/each}
<CreateButton title="Add Method" on:click={addMethod}/>
