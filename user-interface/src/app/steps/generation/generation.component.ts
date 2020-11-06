import { GenerationSettings } from './../../model/generation-settings';
import { ToastrService } from 'ngx-toastr';
import { GenerationSettingsService } from './../../service/generation-settings.service';
import { SettingsStepService } from '../../service/settings-step.service';
import { Component } from '@angular/core';
import { StepComponent } from '../step.component';
import { StepCompletion } from 'src/app/model/step-completion';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { Step } from 'src/app/model/step';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { tap, catchError, finalize, take, map } from 'rxjs/operators';
import { EMPTY } from 'rxjs';

@Component({
  selector: 'app-generation',
  templateUrl: './generation.component.html',
  styleUrls: ['./generation.component.css']
})
export class GenerationComponent extends StepComponent {

  generationForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private settingsStepService: SettingsStepService,
              private generationSettingsService: GenerationSettingsService, private toastrService: ToastrService) {
    super();
    settingsStepService.getSettings$.pipe(map(settings => {
      if (settings){
        return settings;
      }
      return {} as GenerationSettings;
    })).subscribe(settings => {
      this.createForm(settings);
    });
  }

  ngOnInit(): void {
  }

  createForm(generationSettings: GenerationSettings) {
    this.generationForm = this.formBuilder.group({
      projectDirectory: [generationSettings.projectDirectory, Validators.required],
      useAnnotations: [generationSettings.useAnnotations, Validators.required],
      useAutoDispatch: [generationSettings.useAutoDispatch, Validators.required]
    });
  }

  generate() {
    const value = this.generationForm.value;
    this.settingsStepService.addGeneralSettingsInfo(value.projectDirectory, value.useAnnotations, value.useAutoDispatch);
    this.settingsStepService.getSettings$.pipe(take(1), tap(settings => {
      this.generationSettingsService.generate(settings).pipe(tap(() => {
        this.toastrService.success('Code generated. Please check folder ' + value.projectDirectory);
      })).subscribe();
    })).subscribe();
    this.stepCompletion.emit(new StepCompletion(
      Step.GENERATION,
      this.generationForm.valid,
      NavigationDirection.FINISH
    ));
  }

  onAnnotationsClick($event) {
    this.generationSettings.useAnnotations = $event.target.checked;
    if (!this.generationSettings.useAnnotations) {
      this.generationSettings.useAutoDispatch = false;
    }
  }

  onAutoDispatchClick($event) {
    this.generationSettings.useAutoDispatch = $event.target.checked;
  }

  next(): void {
    throw new Error('Operation Not Supported.');
  }

  previous(): void {
    const value = this.generationForm.value;
    this.settingsStepService.addGeneralSettingsInfo(value.projectDirectory, value.useAnnotations, value.useAutoDispatch);
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
