import { Component } from '@angular/core';
import { StepCompletion } from './model/step-completion';
import { Step, StepPath } from './model/step';
import { Router } from '@angular/router';
import { StepComponent } from './steps/step.component';
import { GenerationSettingsModel } from './model/generation-settings.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  activeComponent: StepComponent;
  generation: GenerationSettingsModel;

  constructor(private router: Router) {
    this.router.navigate(['/context']);
    this.generation = new GenerationSettingsModel();
  }

  handleStepCompletion(stepCompletion: StepCompletion) {
    let path = StepPath.get(stepCompletion.nextStep());
    this.router.navigate([path]);
  }

  onComponentActivated(componentReference: StepComponent) {
    this.activeComponent = componentReference;
    this.activeComponent.generation = this.generation; 
    this.activeComponent.stepCompletion.subscribe((data: StepCompletion) => {
      this.handleStepCompletion(data);
    })
  }

  next() {
    this.activeComponent.next();
  }

  previous() {
    this.activeComponent.previous();
  }

  generate() {
    this.activeComponent.generate();
  }

  hasNext() {
    return this.activeComponent.hasNext();
  }

  hasPrevious() {
    return this.activeComponent.hasPrevious();
  }

  isLastStep() {
    return this.activeComponent.isLastStep();
  }
  
}
