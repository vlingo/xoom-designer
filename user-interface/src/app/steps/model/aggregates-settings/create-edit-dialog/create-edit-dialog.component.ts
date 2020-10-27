import {
  AggregateSetting,
  Method,
  AggregateEvent,
  StateField,
  Api,
  Route
} from './../../../../model/model-aggregate';
import {
  FormArray,
  FormGroup,
  FormBuilder,
  AbstractControl,
  Validators
} from '@angular/forms';
import {
  Component,
  Inject,
  OnInit
} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA
} from '@angular/material/dialog';

@Component({
  selector: 'app-create-edit-dialog',
  templateUrl: './create-edit-dialog.component.html',
  styleUrls: ['./create-edit-dialog.component.css']
})
export class CreateEditDialogComponent implements OnInit {

  aggregateSettingsForm: FormGroup;
  stateFields: FormArray;
  stateFieldsTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char'];
  httpsMethods = ['POST', 'PUT', 'DELETE', 'PATCH', 'GET', 'HEAD', 'OPTIONS'];
  creator = {
    stateFields: this.createStateField,
    events: this.createEvents,
    methods: this.createMethods,
    api: this.createApi
  };

  constructor(private formBuilder: FormBuilder,
              private dialogRef: MatDialogRef < CreateEditDialogComponent > ,
              @Inject(MAT_DIALOG_DATA) public aggregate: AggregateSetting) {
    this.createNewForm(aggregate || {} as AggregateSetting);
  }

  ngOnInit(): void {
    this.formMethods.valueChanges.subscribe(formMethods => {
      formMethods.forEach(formMethod => {
        this.formApiRoutes.controls.forEach(control => {
          if (control.get('aggregateMethod').value === formMethod.name) {
            if (formMethod.factory === 'YES') {
              control.get('requireEntityLoad').disable();
              control.get('requireEntityLoad').setValue('NO');
            } else {
              control.get('requireEntityLoad').setValue('YES');
              control.get('requireEntityLoad').enable();
            }
          }
        });
      });

      this.formApiRoutes.controls.forEach(formRoute => {
        formRoute.get('aggregateMethod').valueChanges.subscribe(aggregateMethod => {
          this.formMethods.controls.forEach(control => {
            if (control.get('name').value === aggregateMethod) {
              if (control.get('factory').value === 'YES') {
                formRoute.get('requireEntityLoad').disable();
                formRoute.get('requireEntityLoad').setValue('NO');
              } else {
                formRoute.get('requireEntityLoad').setValue('YES');
                formRoute.get('requireEntityLoad').enable();
              }
            }
          });
        });
      });
    });
  }

  get formStateFields(): FormArray {
    return this.aggregateSettingsForm.get('stateFields') as FormArray;
  }

  get formEvents(): FormArray {
    return this.aggregateSettingsForm.get('events') as FormArray;
  }

  get formMethods(): FormArray {
    return this.aggregateSettingsForm.get('methods') as FormArray;
  }

  get formApi(): AbstractControl {
    return this.aggregateSettingsForm.get('api');
  }

  get formApiRoutes(): FormArray {
    return this.formApi.get('routes') as FormArray;
  }

  createNewForm(aggregate: AggregateSetting) {
    const stateFields = this.formBuilder.array(
      (aggregate.stateFields && aggregate.stateFields.length > 0) ? aggregate.stateFields.map(sf => {
        return this.createStateField(this.formBuilder, sf);
      }) : [this.createStateField(this.formBuilder, {} as StateField)]);

    const events = this.formBuilder.array(
      (aggregate.events && aggregate.events.length > 0) ? aggregate.events.map(ev => {
        return this.createEvents(this.formBuilder, ev);
      }) : [this.createEvents(this.formBuilder, {} as AggregateEvent)]);

    const methods = this.formBuilder.array(
      (aggregate.methods && aggregate.methods.length > 0) ? aggregate.methods.map(method => {
        return this.createMethods(this.formBuilder, method);
      }) : [this.createMethods(this.formBuilder, {} as Method)]);

    this.aggregateSettingsForm = this.formBuilder.group({
      aggregateName: [aggregate.aggregateName, [Validators.required]],
      stateFields,
      events,
      methods,
      api: this.createApi(this.formBuilder, aggregate.api || {} as Api)
    });
  }

  addNewRow(type: string) {
    const formArray = this.aggregateSettingsForm.get(type) as FormArray;
    formArray.push(this.creator[type](this.formBuilder, {}));
  }

  removeRow(type: string, index: number): void {
    (this.aggregateSettingsForm.get(type) as FormArray).removeAt(index);
  }

  addApiRow() {
    const formArray = this.formApiRoutes;
    formArray.push(this.createApiRoutes(this.formBuilder, {} as Route));
  }

  removeApiRow(index: number) {
    this.formApiRoutes.removeAt(index);
  }

  add() {
    this.dialogRef.close(
      this.parseAggregateForm()
    );
  }

  cancel() {
    this.dialogRef.close();
  }

  private createStateField(formBuilder: FormBuilder, stateField: StateField): FormGroup {
    return formBuilder.group({
      name: [stateField.name, [Validators.required]],
      type: [stateField.type, [Validators.required]],
    });
  }

  private createEvents(formBuilder: FormBuilder, event: AggregateEvent): FormGroup {
    return formBuilder.group({
      name: [event.name, [Validators.required]],
      fields: [(event.fields && event.fields.length > 0) ? event.fields : [], [Validators.required]]
    });
  }

  private createMethods(formBuilder: FormBuilder, method: Method): FormGroup {
    return formBuilder.group({
      name: [method.name, [Validators.required]],
      factory: [(method.factory) ? 'YES' : 'NO', [Validators.required]],
      parameters: [(method.parameters && method.parameters.length > 0) ? method.parameters : [], [Validators.required]],
      event: [method.event, [Validators.required]]
    });
  }

  private createApi(formBuilder: FormBuilder, api: Api): FormGroup {
    const routes = this.formBuilder.array(
      (api.routes && api.routes.length > 0) ? api.routes.map(route => {
        return this.createApiRoutes(formBuilder, route);
      }) : [this.createApiRoutes(formBuilder, {} as Route)]);

    return formBuilder.group({
      rootPath: [api.rootPath, [Validators.required]],
      routes
    });
  }

  private createApiRoutes(formBuilder: FormBuilder, route: Route): FormGroup {
    return formBuilder.group({
      path: [route.path, [Validators.required]],
      httpMethod: [route.httpMethod, [Validators.required]],
      aggregateMethod: [route.aggregateMethod, [Validators.required]],
      requireEntityLoad: [(route.requireEntityLoad) ? 'YES' : 'NO', [Validators.required]],
    });
  }

  private parseAggregateForm(): AggregateSetting {
    const formValue = this.aggregateSettingsForm.value as AggregateSetting;
    const methods = (this.aggregateSettingsForm.value.methods).map(method => {
      method.parameters = Array.isArray(method.parameters) ? method.parameters : [];
      return method as Method;
    });
    const events = (this.aggregateSettingsForm.value.events).map(event => {
      event.fields = Array.isArray(event.fields) ? event.fields : [];
      return event as AggregateEvent;
    });
    const api = this.aggregateSettingsForm.value.api;
    api.parameters = Array.isArray(api.parameters) ? api.parameters : [];
    return {
      aggregateName: formValue.aggregateName,
      stateFields: formValue.stateFields,
      methods,
      events,
      api
    } as AggregateSetting;
  }

}
