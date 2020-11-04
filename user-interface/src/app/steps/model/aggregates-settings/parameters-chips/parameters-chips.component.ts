import { StateField } from './../../../../model/model-aggregate';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormControl, Validators } from '@angular/forms';


@Component({
  selector: 'app-parameters-chips',
  templateUrl: './parameters-chips.component.html',
  styleUrls: ['./parameters-chips.component.css']
})
export class ParametersChipsComponent implements OnInit {

  formParameter = new FormControl('', []);
  filteredParameters: StateField[] = [];
  parameters: StateField[] = [];
  choosedValues: string[] = [];
  parametersControl: AbstractControl;
  @Input()
  parentForm: FormArray;
  @Input()
  parametersFormName: string;
  @Input()
  formStateFields: FormArray;
  @Input()
  formIndex: number;

  constructor() {
  }

  ngOnInit(): void {
    this.formStateFields.valueChanges.subscribe(stateFields => {
      this.filteredParameters = this.formStateFields.controls
                                .map(control => control.value  as StateField)
                                .filter(sf => !!sf.name && !this.choosedValues.includes(sf.name));
    });
    this.parametersControl = this.parentForm.at(this.formIndex).get(this.parametersFormName);
    const valuesAlreadyAdded = this.parametersControl.value;
    if (valuesAlreadyAdded.length > 0){
      this.choosedValues = valuesAlreadyAdded;
      const stateFields = this.formStateFields.controls.map(control => control.value  as StateField);
      this.parameters = this.choosedValues
                            .map(value => stateFields.filter(sf => sf.name === value).pop());
    }
    this.filteredParameters = this.formStateFields.controls
                                .map(control => control.value  as StateField)
                                .filter(sf => !!sf.name && !this.choosedValues.includes(sf.name));
  }

  drop(event: CdkDragDrop<string>) {
    this.moveItemInArray(this.parameters, event.previousIndex, event.currentIndex);
    this.updateChoosedValue();
  }

  remove(parameter: StateField): void {
    const index = this.parameters.indexOf(parameter);
    if (index >= 0) {
      this.parameters.splice(index, 1);
    }
    this.filteredParameters.push(parameter);
    this.updateChoosedValue();
  }

  selected($event): void {
    this.parameters.push($event.value);
    this.filteredParameters = this.filteredParameters.filter(f => f !== $event.value);
    this.formParameter.setValue('');
    this.updateChoosedValue();
  }

  updateChoosedValue(){
    this.choosedValues = this.parameters.map(p => p.name);
    this.parametersControl.setValue(this.choosedValues);
    console.log(this.choosedValues);
  }

  private moveItemInArray(arr: StateField[], oldIndex: number, newIndex: number) {
    while (oldIndex < 0) {
        oldIndex += arr.length;
    }
    while (newIndex < 0) {
        newIndex += arr.length;
    }
    if (newIndex >= arr.length) {
        let k = newIndex - arr.length + 1;
        while (k--) {
            arr.push(undefined);
        }
    }
    arr.splice(newIndex, 0, arr.splice(oldIndex, 1)[0]);
  }

}
