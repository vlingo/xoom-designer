import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../steps/step.component';
import { GenerationSettings } from '../model/generation-settings';
import { Step, StepPath, StepName } from '../model/step';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { StepCompletion } from '../model/step-completion';
import { RoutingHistoryService } from '../routing/routing-history.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  activeComponent: StepComponent;
  generationSettings: GenerationSettings;
  stepStatus = new Map<Step, Boolean>();

  constructor(private router: Router, 
              private routingHistoryService: RoutingHistoryService, 
              private toastrService: ToastrService) {
    if(routingHistoryService.isFirstAccess()) {
      this.router.navigate(['/settings/context']);
    }
    this.generationSettings = new GenerationSettings();
  }

  ngOnInit(): void {
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
