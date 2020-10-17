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
      if (model && model.aggregatesSettings){
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

  remove(aggregatesSetting: AggregatesSetting): void {
    this.aggregatesSettings = this.aggregatesSettings.filter(ag => ag.aggregateName !== aggregatesSetting.aggregateName);
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
    this.openAggregateModal({} as AggregatesSetting);
  }

  openAggregateModal(aggregate: AggregatesSetting) {
    const dialogRef = this.dialog.open(CreateEditDialogComponent, {
      data: aggregate,
      height: '83%',
      width: '70%',
    });
    dialogRef.afterClosed().subscribe(editedAggregate => {
      if (editedAggregate && editedAggregate.aggregateName) {
        this.aggregatesSettings = this.aggregatesSettings.filter(ag => ag.aggregateName !== editedAggregate.aggregateName);
        this.aggregatesSettings.push(editedAggregate);
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
