<script>
	import { settings, simpleTypes, valueObjectNameChanges } from "../../stores";
	import { Dialog, CardActions, Row, Col } from "svelte-materialify/src";
	import { requireRule } from "../../validators";
  import { formatArrayForSelect } from "../../utils";
  import DeleteWithDialog from "./DeleteWithDialog.svelte";
  import FieldTypeSelect from './FieldTypeSelect.svelte';
  import ErrorWarningTooltip from './ErrorWarningTooltip.svelte';
  import { Button } from '../ui';
  import IconButton from '@smui/icon-button';
  import Menu from '@smui/menu';
  import List, { Item } from '@smui/list';
  import { Anchor } from '@smui/menu-surface';
  import Textfield from '@smui/textfield';
  import CollectionTypeSelect from "./CollectionTypeSelect.svelte";
  import { tick } from "svelte";

  let dialogActive = false;
  let menu;
  let anchor;
  let anchorClasses = {};
  let renameDialogActive = false;
  let deleteDialogActive = false;
  let updateValueName = null;
  let selectedValueObjectForDelete = null;
  export let events;
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
  let valueObjectNameElement;

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
    if(valueObjectForm.name != updateValueName) {
      renameDialogActive = true;
    } else {
      save();
      dialogActive = false;
    } 
  }

  function rename() {
    valueObjectNameChanges.log(updateValueName, valueObjectForm.name);
    dialogActive = false;
    renameDialogActive = false;
    save();
  }

  function save() {
    let i = $settings.model.valueObjectSettings.findIndex(valueObject => valueObject.name === updateValueName);
    $settings.model.valueObjectSettings.splice(i, 1, valueObjectForm);
    $settings.model.valueObjectSettings = $settings.model.valueObjectSettings;
  }

  function edit(value) {
    updateValueName = value.name;
    valueObjectForm = {...value};
    dialogActive = true;
  }
  function newField() {
    valueObjectForm.fields = [...valueObjectForm.fields, { name: '', type: '', collectionType: '' }]
    tick().then(() => {
      const el = document.querySelector(`#objectValueName${valueObjectForm.fields.length - 1} input`);
      if (el) el.focus()
    })
  }
  function removeField(i) {
		valueObjectForm.fields.splice(i, 1);
		valueObjectForm.fields = valueObjectForm.fields;
    tick().then(() => {
      const el = document.querySelector(`#objectValueName${i === 0 ? 0 : i - 1} input`);
      if (el) el.focus()
    })
  }
  
  const isObjectFieldNameUnique = (value) => {
    if (updateValueName === value) return undefined;

    if(events)
      return events.some((item) => item.name === value) ?  `${value} must be unique but is already defined as an Event` : undefined;

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

  function handleKeydown(e, vo) {
    if (e.key === 'Delete' || e.keyCode === 46) {
      showDeleteDialog(vo);
    }
  }

  $: if (valueObjectForm.fields) {
    valueObjectForm.fields = valueObjectForm.fields.map(f => {
      return {
        ...f,
        collectionType: f.collectionType === null || typeof f.collectionType === "string" ? f.collectionType : null
      }
    })
  }

  $: if (dialogActive) {
    valueObjectNameElement && valueObjectNameElement.focus()
  }

  $: valid = !!valueObjectForm.name
    && valueObjectForm.fields.every((field) => !!field.name && !!field.type)
    && $settings.model.valueObjectSettings.every((item) => !isObjectFieldNameUnique(valueObjectForm.name))
    && valueObjectForm.fields.every((field) => !isFieldUnique(field.name));
</script>

<div class="d-flex mb-4">
  <Button variant="raised" color="secondary" class="mr-4" on:click={newvalueObject}>
    <svelte:fragment slot="appendIcon">add</svelte:fragment>
    New Value Object
  </Button>
  {#if $settings.model.valueObjectSettings.length > 0}
    <div
      class={Object.keys(anchorClasses).join(' ')}
      use:Anchor={{
        addClass: (className) => {
          if (!anchorClasses[className]) {
            anchorClasses[className] = true;
          }
        },
        removeClass: (className) => {
          if (anchorClasses[className]) {
            delete anchorClasses[className];
            anchorClasses = anchorClasses;
          }
        },
      }}
      bind:this={anchor}
    >
      <Button color="primary" variant="raised" on:click={() => menu.setOpen(true)}>
        <svelte:fragment slot="appendIcon">edit</svelte:fragment>
        Edit Value Object
      </Button>
      <Menu
        bind:this={menu}
        anchor={false}
        bind:anchorElement={anchor}
        anchorCorner="BOTTOM_LEFT"
        style="width: 100%;"
      >
        <List>
          {#each $settings.model.valueObjectSettings as valueObject, id (id)}
            <Item class="d-flex" on:SMUI:action={() => edit(valueObject)} on:keydown={(e) => handleKeydown(e, valueObject)}>
              <Button style="flex:1; justify-content: flex-start; text-transform: none;" on:click={() => edit(valueObject)}>
                <svelte:fragment slot="prependIcon">edit</svelte:fragment>
                {valueObject.name}
              </Button>
              <div on:click|stopPropagation>
                <IconButton class="material-icons error-text" on:click={() => showDeleteDialog(valueObject)} title="Delete Value Object" ripple={false}>delete</IconButton>
              </div>
            </Item>
          {/each}
        </List>
      </Menu>
    </div>
  {/if}
</div>

<Dialog class="d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-left" bind:active={deleteDialogActive}>
  <div>
    The <b>{selectedValueObjectForDelete.name}</b> might be in use by other Value Object types or Aggregate state fields.
    Are you sure you want to delete Value Object type <b>{selectedValueObjectForDelete.name}</b>?
  </div>
  <CardActions class="d-flex justify-end mt-4">
    <Button class="error-color white-text mr-4" on:click={remove}>Delete</Button>
    <Button color="secondary" variant="raised" on:click={() => deleteDialogActive = !deleteDialogActive}>Cancel</Button>
  </CardActions>
</Dialog>


<Dialog class="vl-dialog d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-center" persistent bind:active={dialogActive}>
  <div>
    <Row class="align-center">
      <Col>
        <Textfield
          bind:this={valueObjectNameElement}
          style="width: 100%;"
          label="Value Object Name"
          required
          bind:value={valueObjectForm.name}
          invalid={[requireRule(valueObjectForm.name), isObjectFieldNameUnique(valueObjectForm.name)].some(f => f)}>
        </Textfield>
      </Col>
      <Col cols="auto" class="pa-0">
        <ErrorWarningTooltip
          names={['Value Object Name', '']}
          messages={[requireRule(valueObjectForm.name), isObjectFieldNameUnique(valueObjectForm.name)]}
        />
      </Col>
    </Row>
    {#each valueObjectForm.fields as field, i (i)}
      <Row class="align-center justify-between">
        <Col>
          <Textfield
            id="objectValueName{i}"
            style="width: 100%;"
            bind:value={field.name}
            label="Field Name"
            required
            invalid={[requireRule(field.name), isFieldUnique(field.name)].some(f => f)}>
          </Textfield>
        </Col>
        <Col>
          <FieldTypeSelect
            items={formatArrayForSelect([...simpleTypes, ...getFieldsFromObjectValuesSettings()])}
            bind:value={field.type}
            collectionType={field.collectionType}
          >
          </FieldTypeSelect>
        </Col>
        <Col>
          <CollectionTypeSelect
            bind:value={field.collectionType}
          />
        </Col>
        <Col cols="auto" class="pa-0 col-auto {valueObjectForm.fields && valueObjectForm.fields.length > 1 ? 'pr-0' : ''}">
          <ErrorWarningTooltip
            names={['Field Name', 'Field Name', 'Field Type']}
            messages={[requireRule(field.name), isFieldUnique(field.name), requireRule(field.type)]}
          />
        </Col>
        {#if valueObjectForm.fields.length > 1}
          <Col cols="auto pa-0">
            <DeleteWithDialog type="Object Field" on:click={() => removeField(i)} color="red"/>
          </Col>
        {/if}
      </Row>
    {/each}
    <Button style="width: 100%;" class="warning-color white-text" on:click={newField} variant="raised">New Field</Button>
  </div>
  <CardActions class="d-flex justify-space-between">
    <Button color="{valid ? 'primary' : ''}" variant="raised" on:click={updateValueName ? update : add} disabled={!valid}>{updateValueName ? 'Update' : 'Add'}</Button>
    <Button class="error-color white-text" variant="raised" on:click={() => dialogActive = !dialogActive}>Cancel</Button>
  </CardActions>
</Dialog>

<Dialog class="d-flex flex-column justify-space-between pa-4 pt-8 pb-8 text-left" bind:active={renameDialogActive}>
  <div>
    The <b>{updateValueName}</b> might be in use by other Value Object types or Aggregate state fields.
    If you agree to renaming, Designer will rename all uses.
    Are you sure you want to rename Value Object type <b>{updateValueName}</b>?
  </div>
  <CardActions class="d-flex justify-end mt-4">
    <Button class="error-color white-text mr-4" on:click={rename}>Update</Button>
    <Button color="secondary" variant="raised" on:click={() => renameDialogActive = !renameDialogActive}>Cancel</Button>
  </CardActions>
</Dialog>


<style global>
  .vl-dialog {
    min-height: 600px;
    --s-dialog-width: 700px;
  }

  .mdc-select, .mdc-select__anchor {
    width: 100%;
  }

  .mdc-select__menu {
    max-height: 400px;
  }
</style>