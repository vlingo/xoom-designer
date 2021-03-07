<script>
	import { valueObjectSettings, simpleTypes } from "../stores";
  import CardForm from "../components/CardForm.svelte";
	import { Button, Dialog, SlideGroup, SlideItem, Icon, Card, CardTitle, CardText, CardActions, TextField, Select, Row, Col } from "svelte-materialify/src";
	import { mdiPlus, mdiPencil } from "@mdi/js";
	import { requireRule } from "../validators";
  import { formatArrayForSelect } from "../utils";
  import DeleteWithDialog from "../components/aggregates/DeleteWithDialog.svelte";

	let dialogActive = false;
  let updateValueName = null;
  let valueObjectForm = {
    name: '',
    fields: [
      {
        name: '',
        type: '',
      }
    ]
  }

  function newvalueObject() {
    updateValueName = null;
    valueObjectForm = {
      name: '',
      fields: [
        {
          name: '',
          type: '',
        }
      ]
    }
    dialogActive = true;
  }
  function add() {
    $valueObjectSettings = [...$valueObjectSettings, valueObjectForm];
    dialogActive = false;
  }
  function remove(i) {
		$valueObjectSettings.splice(i, 1);
		$valueObjectSettings = $valueObjectSettings;
  }
  function update() {
    const i = $valueObjectSettings.findIndex(valueObject => valueObject.name === updateValueName)
    $valueObjectSettings.splice(i, 1, valueObjectForm);
		$valueObjectSettings = $valueObjectSettings;
    dialogActive = false;
  }
  function edit(value) {
    updateValueName = value.name;
    valueObjectForm = {...value};
    dialogActive = true;
  }
  function newField() {
    valueObjectForm.fields = [...valueObjectForm.fields, { name: '', type: '' }]
  }
  function removeField(i) {
		valueObjectForm.fields.splice(i, 1);
		valueObjectForm.fields = valueObjectForm.fields;
  }
  const isObjectFieldNameUnique = (value) => {
    if (updateValueName === value) return undefined;
    return $valueObjectSettings.some((item) => item.name === value) ?  `${value} already exists.` : undefined;
  };

  const isFieldUnique = (value) => {
    return valueObjectForm.fields.filter((item) => item.name === value).length > 1 ? `${value} already exists.` : undefined;
  }

  $: valid = !!valueObjectForm.name
    && valueObjectForm.fields.every((field) => !!field.name && !!field.type)
    && $valueObjectSettings.every((item) => !isObjectFieldNameUnique(valueObjectForm.name))
    && valueObjectForm.fields.every((field) => !isFieldUnique(field.name));
</script>

<svelte:head>
	<title>Object Value Settings</title>
</svelte:head>

<CardForm title="Object Value Settings" previous="context" next="aggregates">
	<Button class="mb-4" hover on:click={newvalueObject}>
		<div title="Add Aggregate" class="d-flex align-center justify-center">
			<Icon class="black-text mr-4" path={mdiPlus}/>
			New Object Value
		</div>
	</Button>
	<div class="d-flex">
		<SlideGroup activeClass="white-text">
			<SlideItem>
				{#each $valueObjectSettings as valueObject, id (id)}
          <Card outlined class="pa-4 mr-4 flex-column justify-space-between" style="display: flex;">
            <CardTitle>{valueObject.name}</CardTitle>
            <CardText style="flex: 1;">
              <div>
                {#each valueObject.fields as field, i (i)}
                  <div class="d-flex">
                    <div class="mr-3">{field.name}</div>
                    <div class="grey-text">{field.type}</div>
                  </div>
                {/each}
              </div>
            </CardText>
            <CardActions>
              <Button title="Edit Object Value" on:click={() => edit(valueObject)} icon class="ma-2">
                <Icon path={mdiPencil}/>
              </Button>
              <DeleteWithDialog fullscreen type="Object VAlue" on:click={() => remove(id)} color="red"/>
            </CardActions>
          </Card>
				{/each}
			</SlideItem>
		</SlideGroup>
	</div>
</CardForm>

<Dialog class="vl-dialog d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-center" persistent bind:active={dialogActive}>
  <div>
    <TextField class="mb-4" bind:value={valueObjectForm.name} rules={[requireRule, isObjectFieldNameUnique]}>Object Value Name</TextField>
    {#each valueObjectForm.fields as field, i (i)}
      <Row>
        <Col>
          <TextField class="mb-4" bind:value={field.name} rules={[requireRule, isFieldUnique]}>Field Name</TextField>
        </Col>
        <Col>
          <Select items={formatArrayForSelect(simpleTypes)} bind:value={field.type}>Regular</Select>
        </Col>
        {#if valueObjectForm.fields.length > 1}
          <Col cols="auto">
            <DeleteWithDialog type="Object Field" on:click={() => removeField(i)} color="red"/>
          </Col>
        {/if}
      </Row>
    {/each}
    <Button class="warning-color" on:click={newField} block>New Field</Button>
  </div>
  <CardActions class="d-flex justify-space-between">
    <Button class="{valid ? 'primary-color' : ''}" on:click={updateValueName ? update : add} disabled={!valid}>{updateValueName ? 'Update' : 'Add'}</Button>
    <Button class="error-color" on:click={() => dialogActive = !dialogActive}>Cancel</Button>
  </CardActions>
</Dialog>

<style global>
  .vl-dialog {
    min-height: 600px;
  }
</style>