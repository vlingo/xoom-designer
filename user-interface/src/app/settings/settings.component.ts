import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../steps/step.component';
import { GenerationSettings } from '../model/generation-settings';
import { Step, StepPath, StepName } from '../model/step';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { StepCompletion } from '../model/step-completion';
import { RoutingHistoryService } from '../routing/routing-history.service';
import { GenerationSettingsService } from '../service/generation-settings.service';
import { LoaderService } from '../service/loader.service';
import { LoaderState } from '../loader/loader-state';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  activeStep: StepComponent;
  finishLabel: String = "Finish";
  ongoingRequest: boolean = false;
  generationSettings: GenerationSettings;
  stepStatus = new Map<Step, Boolean>();

  constructor(private router: Router, 
              private routingHistoryService: RoutingHistoryService, 
              private service: GenerationSettingsService,
              private loaderService: LoaderService,
              private toastrService: ToastrService) {
    
    if(routingHistoryService.isFirstAccess()) {
      this.router.navigate(['/settings/context']);
    }
    
    this.loaderService.loaderState
    .subscribe((state: LoaderState) => {
      this.ongoingRequest = state.show;
    });
    
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
      if(this.validate()) {
        this.submit();
      }
    }
  }

  private validate() {
    let invalidSteps = new Array<String>();
    
    this.stepStatus.forEach((value: Boolean, key: Step) => {
      if(!value) {
        invalidSteps.push(StepName.get(key));
      }
    });

    if(invalidSteps.length != 0) {
      this.toastrService.warning("Please inform the required settings in the following step(s): " + invalidSteps.join(", "));
      return false;
    } 

    return true;
  }

  private submit() {
    this.service.generate(this.generationSettings).subscribe(data => {
      this.toastrService.success("Project has been successfully generated.");
    });
  }

  onComponentActivated(componentReference: StepComponent) {
    this.activeStep = componentReference;
    this.activeStep.generationSettings = this.generationSettings; 
    this.activeStep.stepCompletion.subscribe((stepCompletion: StepCompletion) => {
      this.handleStepCompletion(stepCompletion);
    })
  }

  next() {
    this.activeStep.next();
  }

  previous() {
    this.activeStep.previous();
  }

  generate() {
    this.activeStep.generate();
  }

  updateFinisthLabel() {
    return this.ongoingRequest ? "Processing..." : "Finish";
  }

  enableNextButton() {
    return !this.ongoingRequest && this.activeStep.hasNext();
  }

  enablePreviousButton() {
    return !this.ongoingRequest && this.activeStep.hasPrevious();
  }

  enableGenerateButton() {
    return !this.ongoingRequest && this.activeStep.isLastStep();
  } 

  isLastStep() {
    return this.activeStep.isLastStep();
  }

  hasActiveStep() {
    return this.activeStep != undefined;
  }
}
