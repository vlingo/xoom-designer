import { AggregatesSetting, Method, AggregateEvent } from './../../../../model/model-aggregate';
import { FormArray, FormGroup, FormBuilder, AbstractControl, Validators } from '@angular/forms';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

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
              private dialogRef: MatDialogRef<CreateEditDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public aggregate: AggregatesSetting) {
    this.creteNewForm();
  }

  ngOnInit(): void {
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

  get formApiParameters(): FormArray {
    return this.formApi.get('parameters') as FormArray;
  }

  creteNewForm(){
      this.aggregateSettingsForm = this.formBuilder.group({
        aggregateName: ['', [Validators.required]],
        stateFields: this.formBuilder.array([this.createStateField(this.formBuilder)]),
        events: this.formBuilder.array([this.createEvents(this.formBuilder)]),
        methods: this.formBuilder.array([this.createMethods(this.formBuilder)]),
        api: this.createApi(this.formBuilder)
      });
  }

  addNewRow(type: string){
    const formArray = this.aggregateSettingsForm.get(type) as FormArray;
    formArray.push(this.creator[type](this.formBuilder));
  }

  removeRow(type: string, index: number): void {
    (this.aggregateSettingsForm.get(type) as FormArray).removeAt(index);
  }

  addApiRow(){
    const formArray = this.formApiParameters;
    formArray.push(this.createApiParameters(this.formBuilder));
  }

  removeApiRow(index: number){
    this.formApiParameters.removeAt(index);
  }

  add(){
    this.dialogRef.close(this.parseAggregateForm());
  }

  cancel(){
    this.dialogRef.close();
  }

  private createStateField(formBuilder: FormBuilder): FormGroup{
    return formBuilder.group({
      name: ['', [Validators.required]],
      type: ['', [Validators.required]],
    });
  }

  private createEvents(formBuilder: FormBuilder): FormGroup{
    return formBuilder.group({
      name: ['', [Validators.required]],
      fields: [formBuilder.group(
        ['', [Validators.required]]
      )]
    });
  }

  private createMethods(formBuilder: FormBuilder): FormGroup{
    return formBuilder.group({
      name: ['', [Validators.required]],
      factory: ['', [Validators.required]],
      parameters: [formBuilder.group(
        ['', [Validators.required]]
      )],
      event: ['', [Validators.required]]
    });
  }

  private createApi(formBuilder: FormBuilder): FormGroup{
    return formBuilder.group({
      rootPath: ['', [Validators.required]],
      parameters: this.formBuilder.array([this.createApiParameters(this.formBuilder)])
    });
  }

  private createApiParameters(formBuilder: FormBuilder): FormGroup{
    return formBuilder.group({
      path: ['', [Validators.required]],
      httpMethod: ['', [Validators.required]],
      aggregateMethod: ['', [Validators.required]],
      requireEntityLoad: ['', [Validators.required]],
    });
  }

  private parseAggregateForm(): AggregatesSetting{
    const formValue = this.aggregateSettingsForm.value as AggregatesSetting;
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
    } as AggregatesSetting;
  }

}
