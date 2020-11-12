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
  Route,
  AggregateSettingWrapper
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
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-aggregates-settings',
  templateUrl: './aggregates-settings.component.html',
  styleUrls: ['./aggregates-settings.component.css']
})
export class AggregatesSettingsComponent extends StepComponent implements OnInit {

  aggregateSettingsWrapper: AggregateSettingWrapper[] = [];

  constructor(private dialog: MatDialog, private settingsStepService: SettingsStepService) {
    super();
    settingsStepService.getSettings$.pipe(map(settings => {
      let wrappers = [];
      if (settings && settings.model && settings.model.aggregateSettings){
        wrappers = settings.model.aggregateSettings.map(setting => new AggregateSettingWrapper(setting));
      }
      return wrappers;
    })).subscribe(wrappers => {
      this.aggregateSettingsWrapper = wrappers;
    });
    if (this.aggregateSettingsWrapper.length === 0) {
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

  remove(id: number): void {
    this.aggregateSettingsWrapper = this.aggregateSettingsWrapper.filter(wrapper => wrapper.id !== id);
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
    this.settingsStepService.addAggregate(this.aggregateSettings);
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
    this.openAggregateModal(new AggregateSettingWrapper(null));
  }

  openAggregateModal(aggregate: AggregateSettingWrapper) {
    const dialogRef = this.dialog.open(CreateEditDialogComponent, {
      data: aggregate,
      height: '83%',
      width: '70%',
    });
    dialogRef.afterClosed().subscribe(editedAggregate => {
      if (editedAggregate) {
        this.remove(editedAggregate.id);
        this.aggregateSettingsWrapper.push(editedAggregate);
      }
    });
  }

  getMethodParameters(method: Method, stateFields: StateField[]) {
    return method.parameters.map(meth => {
      return stateFields.filter(sf => sf.name === meth).pop();
    }).map(state => {
      return state.type + ' ' + state.name;
    }).join(', ');
  }

  getApiEntirePath(api: Api, route: Route): string {
    return route.httpMethod + ' - ' + (this.addSlashs(api.rootPath, route.path) + this.addStartSlash(route.path)).replace(/\/\//g, '/');
  }

  get aggregateSettings(): AggregateSetting[]{
    return this.aggregateSettingsWrapper.map(ag => ag.aggregateSettings);
  }

  aggregateSettingsWrapperSorted(): AggregateSettingWrapper[]{
    return this.aggregateSettingsWrapper.sort((first, second) => first.id - second.id);
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
