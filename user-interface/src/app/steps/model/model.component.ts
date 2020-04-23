import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { StepCompletion } from 'src/app/model/step-completion';
import { Step } from 'src/app/model/step';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepComponent } from '../step.component';

@Component({
  selector: 'app-model',
  templateUrl: './model.component.html',
  styleUrls: ['./model.component.css']
})

export class ModelComponent extends StepComponent {

  constructor() { super(); }
  
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
      Step.MODEL,
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
