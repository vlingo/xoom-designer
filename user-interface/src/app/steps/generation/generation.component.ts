import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../step.component';
import { StepCompletion } from 'src/app/model/step-completion';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { Step } from 'src/app/model/step';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-generation',
  templateUrl: './generation.component.html',
  styleUrls: ['./generation.component.css']
})
export class GenerationComponent extends StepComponent {
  
  generationForm: FormGroup; 
  
  constructor(private formBuilder: FormBuilder) {
    super();
    this.createForm();
  }
  
  ngOnInit(): void {
  }
  
  createForm() {
    this.generationForm = this.formBuilder.group({
      ProjectDirectory: ['', Validators.required]
    });
  }

  generate() {
    this.stepCompletion.emit(new StepCompletion(
      Step.GENERATION,
      this.generationForm.valid,
      NavigationDirection.FINISH
    ));
  }

  onAnnotationsClick($event) {
    this.generationSettings.useAnnotations = $event.target.checked;
    if(!this.generationSettings.useAnnotations) {
      this.generationSettings.useAutoDispatch = false;
    }
  }

  onAutoDispatchClick($event) {
    this.generationSettings.useAutoDispatch = $event.target.checked;
  }

  next(): void {
    throw new Error("Operation Not Supported.");
  }

  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.GENERATION,
      this.generationForm.valid,
      NavigationDirection.REWIND
    ));
  }

  hasNext(): Boolean {
    return false;
  }
  
  hasPrevious(): Boolean {
    return true;
  }

  isLastStep() : Boolean {
    return true;
  }

}
