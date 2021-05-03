<script>
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
	import { identifierRule, requireRule, isPropertyUniqueRule } from "../../validators";
  import FillFieldsNote from './FillFieldsNote.svelte';
  import MethodForm from './MethodForm.svelte';

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
					if (method.parameters && method.parameters.includes(cur.name) && acc.findIndex(a => a === cur.name) < 0) acc.push(cur.name);
					return acc;
        }, []),
        event: events.some((e) => e.name === method.event) ? method.event : undefined
			};
		})
  }
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">Methods</h6>
  </legend>
  {#if methods.length < 1}
    <div class="text-center">There is no method! Add One.</div>
  {/if}
  {#each methods as method, id (id)}
    <div class="d-flex align-center">
      <div style="flex: 1;">
        <MethodForm {stateFields} {events} {methods} bind:method />
      </div>
      <div style="align-self: flex-start; width: 32px;">
        <DeleteWithDialog type="Method" on:click={() => deleteMethod(id)}>
          <b>{method.name}</b> might be in use at API and Consumer Exchange sections.
        </DeleteWithDialog>
      </div>
    </div>
  {/each}
  {#if methods.filter(method => requireRule(method.name) || identifierRule(method.name) || isPropertyUniqueRule(method.name, methods, 'name') || method.parameters.length < 1 || !method.event).length > 0}
    <FillFieldsNote />
  {/if}
  <CreateButton title="Add Method" on:click={addMethod}/>
</fieldset>