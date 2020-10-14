import { AggregatesSetting, Persistence, Model } from './../../model/model-aggregate';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()
export class ModelService {
  private model$ = new BehaviorSubject<Model>(null);
  getModel$: Observable<Model> = this.model$.asObservable();

  constructor() { }

  addAggregate(aggregates: AggregatesSetting[]): void {
    const model = this.model$.getValue() || {} as Model;
    model.aggregatesSettings = aggregates;
    this.model$.next(model);
  }

  addPersistence(persistence: Persistence): void {
    const model = this.model$.getValue() || {} as Model;
    model.persistence = persistence;
    this.model$.next(model);
  }

}
