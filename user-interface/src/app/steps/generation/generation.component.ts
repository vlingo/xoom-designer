import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../step.component';
import { StepCompletion } from 'src/app/model/step-completion';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { Step } from 'src/app/model/step';

@Component({
  selector: 'app-generation',
  templateUrl: './generation.component.html',
  styleUrls: ['./generation.component.css']
})
export class GenerationComponent extends StepComponent {

  constructor() {
    super();
  }

  ngOnInit(): void {
  }

  next(): void {
    throw new Error("Operation Not Supported.");
  }

  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.GENERATION,
      true,
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
