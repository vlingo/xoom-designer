import { SettingsStepService } from './../../service/settings-step.service';
import { ContextSettings } from './../../model/context-settings';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { StepCompletion } from '../../model/step-completion';
import { Step } from '../../model/step';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepComponent } from '../step.component';

@Component({
  selector: 'app-context',
  templateUrl: './context.component.html',
  styleUrls: ['./context.component.css']
})
export class ContextComponent extends StepComponent implements OnInit {

  contextForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private settingsStepService: SettingsStepService) {
    super();
    this.createForm();
  }

  ngOnInit(): void {
  }

  createForm() {
    this.contextForm = this.formBuilder.group({
      groupId: ['', Validators.required],
      artifactId: ['', Validators.required],
      artifactVersion: ['', Validators.required],
      packageName: ['', Validators.required],
      xoomVersion: ['1.3.4-SNAPSHOT']
    });
  }

  next() {
    const context = this.contextForm.value as ContextSettings;
    this.settingsStepService.addContext(context);
    this.stepCompletion.emit(new StepCompletion(
        Step.CONTEXT, this.contextForm.valid, NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    throw new Error("Operation not supported.");
  }

  xoomVersions() : Array<String> {
    return [
      "1.3.4-SNAPSHOT"
    ];
  }

  hasNext(): Boolean {
    return true;
  }

  hasPrevious(): Boolean {
    return false;
  }

  canFinish(): Boolean {
    return true;
  }

}
