import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../step.component';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepCompletion } from 'src/app/model/step-completion';
import { Step } from 'src/app/model/step';

@Component({
  selector: 'app-deployment',
  templateUrl: './deployment.component.html',
  styleUrls: ['./deployment.component.css']
})
export class DeploymentComponent extends StepComponent {

  constructor() { 
    super();
  }

  ngOnInit(): void {
  }
  
  next() {
    this.move(NavigationDirection.FORWARD);
  }  

  previous() {
    this.move(NavigationDirection.REWIND);
  }

  private move(navigationDirection: NavigationDirection) {
    this.stepCompletion.emit(new StepCompletion(
      Step.DEPLOYMENT,
      true,
      navigationDirection
    ));
  }

  hasNext(): Boolean {
    return true;
  }
  
  hasPrevious(): Boolean {
    return true;
  }

  canFinish(): Boolean {
    return false;
  }
  
}
