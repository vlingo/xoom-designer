<script>
	import { settings, simpleTypes, collectionTypes } from "../../stores";
	import { Button, Dialog, Icon, CardActions, TextField, Select, Row, Col, Menu, List, ListItem } from "svelte-materialify/src";
	import { mdiPlus, mdiDelete } from "@mdi/js";
	import { requireRule } from "../../validators";
  import { formatArrayForSelect } from "../../utils";
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
  import FieldTypeSelect from './FieldTypeSelect.svelte';

	let dialogActive = false;
  let deleteDialogActive = false;
  let updateValueName = null;
  let selectedValueObjectForDelete = null;
  let valueObjectForm = {
    name: '',
    fields: [
      {
        name: '',
        type: '',
        collectionType: ''
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
          collectionType: ''
        }
      ]
    }
    dialogActive = true;
  }
  function add() {
    $settings.model.valueObjectSettings = [...$settings.model.valueObjectSettings, valueObjectForm];
    dialogActive = false;
  }
  function remove() {
		$settings.model.valueObjectSettings.splice($settings.model.valueObjectSettings.findIndex(item => item.name == selectedValueObjectForDelete.name), 1);
		$settings.model.valueObjectSettings = $settings.model.valueObjectSettings;
    deleteDialogActive = false;
  }
  function showDeleteDialog(valueObject) {
    selectedValueObjectForDelete = valueObject;
    deleteDialogActive = true;
  }
  function update() {
    const i = $settings.model.valueObjectSettings.findIndex(valueObject => valueObject.name === updateValueName)
    $settings.model.valueObjectSettings.splice(i, 1, valueObjectForm);
		$settings.model.valueObjectSettings = $settings.model.valueObjectSettings;
    dialogActive = false;
  }
  function edit(value) {
    updateValueName = value.name;
    valueObjectForm = {...value};
    dialogActive = true;
  }
  function newField() {
    valueObjectForm.fields = [...valueObjectForm.fields, { name: '', type: '', collectionType: '' }]
  }
  function removeField(i) {
		valueObjectForm.fields.splice(i, 1);
		valueObjectForm.fields = valueObjectForm.fields;
  }
  const isObjectFieldNameUnique = (value) => {
    if (updateValueName === value) return undefined;
    return $settings.model.valueObjectSettings.some((item) => item.name === value) ?  `${value} already exists.` : undefined;
  };

  const isFieldUnique = (value) => {
    return valueObjectForm.fields.filter((item) => item.name === value).length > 1 ? `${value} already exists.` : undefined;
  }

  const getFieldsFromObjectValuesSettings = () => {
    const objectValueNames = $settings.model.valueObjectSettings.map(item => {
      if (valueObjectForm.name !== item.name) {
        return item.name;
      }
    });
    return objectValueNames;
  }

  $: if (valueObjectForm.fields) {
    valueObjectForm.fields = valueObjectForm.fields.map(f => {
      return {
        ...f,
        collectionType: f.collectionType === null || typeof f.collectionType === "string" ? f.collectionType : null
      }
    })
  }

  $: valid = !!valueObjectForm.name
    && valueObjectForm.fields.every((field) => !!field.name && !!field.type)
    && $settings.model.valueObjectSettings.every((item) => !isObjectFieldNameUnique(valueObjectForm.name))
    && valueObjectForm.fields.every((field) => !isFieldUnique(field.name));
</script>

<div class="d-flex mb-4">
  <Button class="mr-4" hover on:click={newvalueObject}>
    <div title="Add Aggregate" class="d-flex align-center justify-center">
      <Icon class="black-text mr-4" path={mdiPlus}/>
      New Value Object
    </div>
  </Button>
  {#if $settings.model.valueObjectSettings.length > 0}
    <Menu>
      <div slot="activator">
        <Button class="success-color">Edit Value Object</Button>
      </div>
      <List>
        {#each $settings.model.valueObjectSettings as valueObject, id (id)}
          <ListItem>
            <div class="d-flex align-center">
              <div style="flex:1;" class="mr-4" on:click={() => edit(valueObject)}>{valueObject.name}</div>
              <Button on:click={() => showDeleteDialog(valueObject)} title="Delete Value Object"  icon class="red-text">
                <Icon class="ma-0" path={mdiDelete}/>
              </Button>
            </div>
          </ListItem>
        {/each}
      </List>
    </Menu>
  {/if}
</div>

<Dialog class="d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-center" bind:active={deleteDialogActive}>
  <div>
    <b>{selectedValueObjectForDelete.name}</b> might be in use by other Value Objects or state fields of aggregates!
    Are you sure you want to delete value object of <b>{selectedValueObjectForDelete.name}</b>?
  </div>
  <CardActions class="d-flex justify-end">
    <Button class="error-color mr-4" on:click={remove}>Delete</Button>
    <Button on:click={() => deleteDialogActive = !deleteDialogActive}>Cancel</Button>
  </CardActions>
</Dialog>

<Dialog class="vl-dialog d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-center" persistent bind:active={dialogActive}>
  <div>
    <TextField class="mb-4" bind:value={valueObjectForm.name} rules={[requireRule, isObjectFieldNameUnique]}>Value Object Name</TextField>
    {#each valueObjectForm.fields as field, i (i)}
      <Row>
        <Col>
          <TextField class="mb-4" bind:value={field.name} rules={[requireRule, isFieldUnique]}>Field Name</TextField>
        </Col>
        <Col>
          <FieldTypeSelect
            mandatory
            items={formatArrayForSelect([...simpleTypes, ...getFieldsFromObjectValuesSettings()])}
            bind:value={field.type}
            collectionType={field.collectionType}
          >
            Type
          </FieldTypeSelect>
        </Col>
        <Col>
          <Select items={formatArrayForSelect(collectionTypes.map(f => f.name))} bind:value={field.collectionType} placeholder="(bare)">Collection</Select>
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