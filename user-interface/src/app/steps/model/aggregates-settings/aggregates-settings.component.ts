import { ModelService } from './../model.service';
import {
  NavigationDirection
} from 'src/app/model/navigation-direction';
import {
  StepCompletion
} from 'src/app/model/step-completion';
import {
  Step
} from 'src/app/model/step';
import {
  ViewDialogComponent
} from './view-dialog/view-dialog.component';
import {
  AggregatesSetting,
  Method,
  StateField
} from './../../../model/model-aggregate';
import {
  Component,
  OnInit
} from '@angular/core';
import {
  MatDialog
} from '@angular/material/dialog';
import {
  StepComponent
} from '../../step.component';
import {
  CreateEditDialogComponent
} from './create-edit-dialog/create-edit-dialog.component';

@Component({
  selector: 'app-aggregates-settings',
  templateUrl: './aggregates-settings.component.html',
  styleUrls: ['./aggregates-settings.component.css']
})
export class AggregatesSettingsComponent extends StepComponent implements OnInit {

  aggregatesSettings: AggregatesSetting[] = [];

  constructor(private dialog: MatDialog, private modelService: ModelService) {
    super();
    modelService.getModel$.subscribe(model => {
      if (model.aggregatesSettings){
        this.aggregatesSettings = model.aggregatesSettings;
      }
    });
    if (this.aggregatesSettings.length === 0) {
      this.openNewAggregateModal();
    }
  }

  ngOnInit(): void {}

  view(aggregatesSetting: AggregatesSetting) {
    this.dialog.open(ViewDialogComponent, {
      data: aggregatesSetting,
      height: '500px',
      width: '500px',
    });
  }

  remove(index: number): void {
    delete this.aggregatesSettings[index];
  }

  next(): void {
    this.modelService.addAggregate(this.aggregatesSettings);
    this.stepCompletion.emit(new StepCompletion(
      Step.AGGREGATE,
      true,
      NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.AGGREGATE,
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

  openNewAggregateModal() {
    const dialogRef = this.dialog.open(CreateEditDialogComponent, {
      data: {},
      height: '700px',
      width: '900px',
    });
    dialogRef.afterClosed().subscribe(aggregate => {
      if (aggregate) {
        this.aggregatesSettings.push(aggregate);
      }
    });
  }

  getMethodParameters(method: Method, stateFields: StateField[]) {
    return stateFields.filter(state => {
      return method.parameters.includes(state.name);
    }).map(state => {
      return state.type + ' ' + state.name;
    }).join(', ');
  }
}
