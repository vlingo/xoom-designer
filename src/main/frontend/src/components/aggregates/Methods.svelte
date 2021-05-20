<script>
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
	import CreateButton from "./CreateButton.svelte";
  import MethodForm from './MethodForm.svelte';
  import pluralize from "pluralize";

  export let methods;
  export let stateFields;
  export let events;

	const addMethod = () => methods = methods.concat({ name: "", useFactory: false, parameters: [], event: "" });
	const deleteMethod = (index) => { methods.splice(index, 1); methods = methods; }
  
	$: {
		methods = methods.map((method) => {
			return {
				...method,
				parameters: stateFields.reduce((acc, cur) => {
          const replace = `^${cur.name}$|^${cur.name} [*#+-]$|^${pluralize.singular(cur.name)} [*#+-]$`;
          const re = new RegExp(replace);
          const pa = method.parameters.find(p => {
            return p.search(re) > -1;
          })
					if (method.parameters && pa && acc.findIndex(a => a === cur.name) < 0) {
            acc.push(pa)
          };
					return acc;
        }, []),
        event: events.some((e) => e.name === method.event) ? method.event : undefined
			};
		})
  }
</script>

<fieldset class="pa-6 pt-8 pb-8 mb-8" style="border: 1px solid rgba(0,0,0,0.15); border-radius: 10px;">
  <legend>
    <h6 class="ma-0 pl-3 pr-3">Command Methods</h6>
  </legend>
  {#if methods.length < 1}
    <div class="text-center">There is no method! Add One.</div>
  {/if}
  {#each methods as method, id (id)}
    <div class="d-flex align-center">
      <MethodForm {stateFields} {events} {methods} bind:method />
      <div style="align-self: flex-start; width: 32px;">
        <DeleteWithDialog type="Method" on:click={() => deleteMethod(id)}>
          <b>{method.name}</b> might be in use at API and Consumer Exchange sections.
        </DeleteWithDialog>
      </div>
    </div>
  {/each}
  <CreateButton title="Add Method" on:click={addMethod}/>
</fieldset>