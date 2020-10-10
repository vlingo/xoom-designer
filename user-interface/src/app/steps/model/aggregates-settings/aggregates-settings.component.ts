import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepCompletion } from 'src/app/model/step-completion';
import { Step } from 'src/app/model/step';
import { ViewDialogComponent } from './view-dialog/view-dialog.component';
import { AggregatesSetting, Method, AggregateEvent, Api, StateField } from './../../../model/model-aggregate';
import {
  FormArray,
  FormGroup,
  FormBuilder,
  Validators
} from '@angular/forms';
import {
  Component,
  OnInit
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StepComponent } from '../../step.component';

@Component({
  selector: 'app-aggregates-settings',
  templateUrl: './aggregates-settings.component.html',
  styleUrls: ['./aggregates-settings.component.css']
})
export class AggregatesSettingsComponent  extends StepComponent implements OnInit {

  aggregateSettingsForm: FormGroup;
  stateFields: FormArray;
  enableAdd = false;
  stateFieldsTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char'];
  creator = {
    stateFields: this.createStateField,
    events: this.createEvents,
    methods: this.createMethods,
    api: this.createApi
  };
  aggregatesSettings: AggregatesSetting[] = [];

  constructor(private formBuilder: FormBuilder,
              private dialog: MatDialog) {
                super();
  }

  ngOnInit(): void {
  }

  removeRow(type: string, index: number): void {
    (this.aggregateSettingsForm.get(type) as FormArray).removeAt(index);
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

  get formApi(): FormArray {
    return this.aggregateSettingsForm.get('api') as FormArray;
  }

  enableNewAggregate(flag: boolean){
    this.enableAdd = flag;
    if (flag) {
      this.aggregateSettingsForm = this.formBuilder.group({
        aggregateName: ['', [Validators.required]],
        stateFields: this.formBuilder.array([this.createStateField(this.formBuilder)]),
        events: this.formBuilder.array([this.createEvents(this.formBuilder)]),
        methods: this.formBuilder.array([this.createMethods(this.formBuilder)]),
        api: this.formBuilder.array([this.createApi(this.formBuilder)])
      });
    }
  }

  add(){
    this.aggregatesSettings.push(this.parseAggregateForm());
    this.enableAdd = false;
  }

  addNewRow(type: string){
    const formArray = this.aggregateSettingsForm.get(type) as FormArray;
    formArray.push(this.creator[type](this.formBuilder));
  }

  view(aggregatesSetting: AggregatesSetting){
    this.dialog.open(ViewDialogComponent, {
      data: aggregatesSetting,
      height: '500px',
      width: '500px',
    });
  }

  remove(index: number): void {
    delete this.aggregatesSettings[index];
  }

  next(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.MODEL,
      true,
      NavigationDirection.FORWARD
    ));
  }
  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.MODEL,
      true,
      NavigationDirection.REWIND
    ));
  }

  hasNext(): Boolean {
    return true;
  }
  hasPrevious(): Boolean {
    return true;
  }

  getMethodParameters(method: Method, stateFields: StateField[]){
    return stateFields.filter(state => {
      return method.parameters.includes(state.name);
    }).map(state => {
      return state.type + ' ' + state.name;
    }).join(', ');
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
      parameters: [formBuilder.group({
        path: ['', [Validators.required]],
        httpMethod: ['', [Validators.required]],
        aggregateMethod: ['', [Validators.required]],
        requireEntityLoad: ['', [Validators.required]],
      })],
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
    const api = (this.aggregateSettingsForm.value.api).map(ap => {
      ap.parameters = Array.isArray(ap.parameters) ? ap.parameters : [];
      return ap as Api;
    });
    return {
      aggregateName: formValue.aggregateName,
      stateFields: formValue.stateFields,
      methods,
      events,
      api
    } as AggregatesSetting;
  }
}
