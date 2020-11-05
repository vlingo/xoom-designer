import { DeploymentSettings } from './../model/deployment-settings';
import { ContextSettings } from '../model/context-settings';
import { GenerationSettings } from '../model/generation-settings';
import { AggregateSetting, Persistence, Model } from '../model/model-aggregate';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class SettingsStepService {
  private settings$ = new BehaviorSubject<GenerationSettings>(null);
  getSettings$: Observable<GenerationSettings> = this.settings$.asObservable();

  constructor() { }

  addAggregate(aggregates: AggregateSetting[]): void {
    const settings = this.getSettings();
    settings.model.aggregateSettings = aggregates;
    this.settings$.next(settings);
  }

  addPersistence(persistence: Persistence): void {
    const settings = this.getSettings();
    settings.model.persistence = persistence;
    this.settings$.next(settings);
  }

  addContext(context: ContextSettings): void {
    const settings = this.getSettings();
    settings.context = context;
    this.settings$.next(settings);
  }

  addDeployment(deployment: DeploymentSettings): void {
    const settings = this.getSettings();
    settings.deployment = deployment;
    this.settings$.next(settings);
  }

  addGeneralSettingsInfo(projectDirectory: string, useAnnotations: boolean, useAutoDispatch: boolean): void {
    const settings = this.getSettings();
    settings.projectDirectory = projectDirectory;
    settings.useAnnotations = useAnnotations;
    settings.useAutoDispatch = useAutoDispatch;
    this.settings$.next(settings);
  }

  private getSettings(): GenerationSettings{
    const settings = this.settings$.getValue() || {} as GenerationSettings;
    settings.model = settings.model || {} as Model;
    settings.context = settings.context || {} as ContextSettings;
    settings.deployment = settings.deployment || {} as DeploymentSettings;
    return settings;
  }

}
