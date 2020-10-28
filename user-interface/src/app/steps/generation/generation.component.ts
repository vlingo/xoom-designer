import { GenerationSettingsService } from './../../service/generation-settings.service';
import { SettingsStepService } from '../../service/settings-step.service';
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

  constructor(private formBuilder: FormBuilder, private settingsStepService: SettingsStepService,
              private generationSettingsService: GenerationSettingsService) {
    super();
    this.createForm();
  }

  ngOnInit(): void {
  }

  createForm() {
    this.generationForm = this.formBuilder.group({
      projectDirectory: ['', Validators.required],
      useAnnotations: [false, Validators.required],
      useAutoDispatch: [false, Validators.required]
    });
  }

  generate() {
    const value = this.generationForm.value;
    this.settingsStepService.getSettings$.subscribe(settings => {
      settings.projectDirectory = value.projectDirectory;
      settings.useAnnotations = value.useAnnotations;
      settings.useAutoDispatch = value.useAutoDispatch;
      this.generationSettingsService.generate(settings).subscribe(response => {
        this.downloadFile(response);
      });
    });
    this.stepCompletion.emit(new StepCompletion(
      Step.GENERATION,
      this.generationForm.valid,
      NavigationDirection.FINISH
    ));
  }

  downloadFile(data: any) {
    const blob = new Blob([data], { type: 'zip' });
    const url = window.URL.createObjectURL(blob);
    window.open(url);
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
    throw new Error('Operation Not Supported.');
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
