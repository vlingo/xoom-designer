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

  constructor(private formBuilder: FormBuilder) {
    super();
    this.createForm();
  }

  ngOnInit(): void {
  }

  createForm() {
    this.contextForm = this.formBuilder.group({
      GroupId: ['', Validators.required],
      ArtifactId: ['', Validators.required],
      ArtifactVersion: ['', Validators.required],
      PackageName: ['', Validators.required],
      XoomVersion: ['', Validators.required]
    });
  }
  
  next() {
    this.stepCompletion.emit(new StepCompletion(
        Step.CONTEXT, this.contextForm.valid, NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    throw new Error("Operation not supported.");
  }

  xoomVersions() : Array<String> {
    return [
      "1.3.0",
      "1.2.20",
      "1.2.19"
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
