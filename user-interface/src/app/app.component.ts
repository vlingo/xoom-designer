import { Component } from '@angular/core';
import { StepCompletion } from './model/step-completion';
import { Step, StepPath, StepName } from './model/step';
import { Router } from '@angular/router';
import { StepComponent } from './steps/step.component';
import { GenerationSettings } from './model/generation-settings';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  activeComponent: StepComponent;
  generationSettings: GenerationSettings;
  stepStatus = new Map<Step, Boolean>();

  constructor(private router: Router, private toastrService: ToastrService) {
    this.router.navigate(['/context']);
    this.generationSettings = new GenerationSettings();
  }

  handleStepCompletion(stepCompletion: StepCompletion) {
   
    this.stepStatus.set(stepCompletion.step, stepCompletion.valid);
   
    if(stepCompletion.requiresNavigation()) {
      let path = StepPath.get(stepCompletion.nextStep());
      this.router.navigate([path]);
    } 

    if(stepCompletion.isGenerationRequested()) {
      this.validateAndSubmit();
    }
  }

  validateAndSubmit() {
    let invalidSteps = new Array<String>();
    
    this.stepStatus.forEach((value: Boolean, key: Step) => {
      if(!value) {
        invalidSteps.push(StepName.get(key));
      }
    });

    if(invalidSteps.length != 0) {
      this.toastrService.warning("Please inform the required settings in the following step(s): " + invalidSteps.join(", "));
    }
  }

  onComponentActivated(componentReference: StepComponent) {
    this.activeComponent = componentReference;
    this.activeComponent.generationSettings = this.generationSettings; 
    this.activeComponent.stepCompletion.subscribe((stepCompletion: StepCompletion) => {
      this.handleStepCompletion(stepCompletion);
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
    if(this.activeComponent == undefined) {
      return false;
    }
    return this.activeComponent.hasNext();
  }

  hasPrevious() {
    if(this.activeComponent == undefined) {
      return false;
    }  
    return this.activeComponent.hasPrevious();
  }

  isLastStep() {
    if(this.activeComponent == undefined) {
      return false;
    }
    return this.activeComponent.isLastStep();
  }
  
}
