import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { StepCompletion } from '../../model/step-completion';
import { ContextModel } from '../../model/context.model';
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
      ParentDirectory: ['', Validators.required],
      XoomVersion: ['', Validators.required]
    });
  }
  
  next() {
    this.stepCompletion.emit(new StepCompletion(
        Step.CONTEXT, this.isCompleted(), NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    throw new Error("Operation not supported.");
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

  private isCompleted() {
    return this.contextForm.valid;
  }

}
