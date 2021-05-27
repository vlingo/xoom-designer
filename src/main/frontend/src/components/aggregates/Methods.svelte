<script>
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
  import MethodForm from './MethodForm.svelte';
  import pluralize from "pluralize";
  import FieldsetBox from "./FieldsetBox.svelte";

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
          const pa = method.parameters.find(p => {
            return p.stateField === cur.name;
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

<FieldsetBox title="Command Methods" on:add={addMethod}>
  {#if methods.length < 1}
    <div class="text-center">There is no method! Add One.</div>
  {/if}
  {#each methods as method, id (id)}
    <div class="d-flex align-center">
      <MethodForm {stateFields} {events} {methods} i={id} bind:method />
      <div style="align-self: flex-start; width: 32px;">
        <DeleteWithDialog type="Method" on:click={() => deleteMethod(id)}>
          <b>{method.name}</b> might be in use at API and Consumer Exchange sections.
        </DeleteWithDialog>
      </div>
    </div>
  {/each}
</FieldsetBox>