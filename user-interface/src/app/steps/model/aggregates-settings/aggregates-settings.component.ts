import { SettingsStepService } from '../../../service/settings-step.service';
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
  AggregateSetting,
  Api,
  Method,
  StateField,
  Route
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

  aggregateSettings: AggregateSetting[] = [];

  constructor(private dialog: MatDialog, private settingsStepService: SettingsStepService) {
    super();
    settingsStepService.getSettings$.subscribe(settings => {
      if (settings.model && settings.model.aggregateSettings){
        this.aggregateSettings = settings.model.aggregateSettings;
      }
    });
    if (this.aggregateSettings.length === 0) {
      this.openNewAggregateModal();
    }
  }

  ngOnInit(): void {}

  view(aggregatesSetting: AggregateSetting) {
    this.dialog.open(ViewDialogComponent, {
      data: aggregatesSetting,
      height: '500px',
      width: '500px',
    });
  }

  remove(aggregateSetting: AggregateSetting): void {
    this.aggregateSettings = this.aggregateSettings.filter(ag => ag.aggregateName !== aggregateSetting.aggregateName);
  }

  next(): void {
    this.settingsStepService.addAggregate(this.aggregateSettings);
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
    this.openAggregateModal({} as AggregateSetting);
  }

  openAggregateModal(aggregate: AggregateSetting) {
    const dialogRef = this.dialog.open(CreateEditDialogComponent, {
      data: aggregate,
      height: '83%',
      width: '70%',
    });
    dialogRef.afterClosed().subscribe(editedAggregate => {
      if (editedAggregate && editedAggregate.aggregateName) {
        this.aggregateSettings = this.aggregateSettings.filter(ag => ag.aggregateName !== editedAggregate.aggregateName);
        this.aggregateSettings.push(editedAggregate);
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

  getApiEntirePath(api: Api, route: Route): string {
    return route.httpMethod + ' - ' + (this.addSlashs(api.rootPath, route.path) + this.addStartSlash(route.path)).replace(/\/\//g, '/');
  }
  private addSlashs(path: string, afterPath: string){
    path = this.addStartSlash(path);
    if (afterPath) {
      path = this.addEndSlash(path);
    }
    return path;
  }

  private addStartSlash(path: string){
    if (!path){
      return '';
    }
    if (!path.startsWith('/')) {
      path = '/' + path;
    }
    return path;
  }

  private addEndSlash(path: string){
    if (!path){
      return '';
    }
    if (!path.endsWith('/')) {
      path = path + '/';
    }
    return path;
  }

}
