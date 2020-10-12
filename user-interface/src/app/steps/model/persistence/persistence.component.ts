import { NavigationDirection } from 'src/app/model/navigation-direction';
import { Step } from 'src/app/model/step';
import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../../step.component';
import { StepCompletion } from 'src/app/model/step-completion';

@Component({
  selector: 'app-persistence',
  templateUrl: './persistence.component.html',
  styleUrls: ['./persistence.component.css']
})
export class PersistenceComponent extends StepComponent implements OnInit {

  constructor() {
    super();
  }

  ngOnInit(): void {
  }

  next(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.PERSISTENCE,
      true,
      NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.PERSISTENCE,
      true,
      NavigationDirection.REWIND
    ));
  }

  hasNext(): Boolean {
    return true;
  }
  hasPrevious(): Boolean {
    return true;
  }

}
