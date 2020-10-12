import { Component, OnInit, Output, EventEmitter, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { StepCompletion } from 'src/app/model/step-completion';
import { Step } from 'src/app/model/step';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepComponent } from '../step.component';
import { TableComponent } from './table/table.component';
import { Aggregate } from 'src/app/model/aggregate';
import { DomainEvent } from 'src/app/model/domain-event';
import { IDropdownSettings, MultiSelectComponent} from 'ng-multiselect-dropdown';
import { MultipleSelectionComponent } from 'src/app/multiple-selection/multiple-selection.component';

@Component({
  selector: 'app-model',
  templateUrl: './model.component.html',
  styleUrls: ['./model.component.css']
})

export class ModelComponent extends StepComponent implements AfterViewInit {

  public selectedAggregateId: Number;
  public restResourcesSettings: IDropdownSettings;
  public databaseTypes: Array<any>;

  @ViewChild('aggregateTable') aggregateTable: TableComponent;
  @ViewChild('domainEventTable') domainEventTable: TableComponent;
  @ViewChild('multipleSelection') multipleSelection: MultipleSelectionComponent;

  constructor() {
    super();
    this.setupRestResources();
  }

  ngOnInit(): void {
    this.loadDatabaseTypes();
  }

  ngAfterViewInit(): void {
    this.multipleSelection.init(this.generationSettings.model.aggregates, this.generationSettings.model.restResources);

    this.multipleSelection.selectedItems.subscribe(items => {
      this.generationSettings.addRestResources(items);
    });

    this.aggregateTable.itemInclusion.subscribe(item => {
      const aggregate = new Aggregate(item.id, item.name);
      this.generationSettings.addAggregate(aggregate);
      this.multipleSelection.add(aggregate);
    });

    this.aggregateTable.itemUpdate.subscribe(item => {
      this.multipleSelection.update(new Aggregate(item.id, item.name));
    });

    this.aggregateTable.itemSelection.subscribe(aggregateId => {
      this.selectedAggregateId = aggregateId;
    });

    this.aggregateTable.itemRemoval.subscribe(id => {
      this.multipleSelection.remove(id);
    });

    this.domainEventTable.itemInclusion.subscribe(item => {
      this.generationSettings.addDomainEvent(this.selectedAggregateId, new DomainEvent(item.id, item.name));
    });
  }

  shouldDisableNewDomainEvents() {
    return this.selectedAggregateId == undefined;
  }

  next() {
    this.multipleSelection.save();
    this.move(NavigationDirection.FORWARD);
  }

  previous() {
    this.multipleSelection.save();
    this.move(NavigationDirection.REWIND);
  }

  onStorageTypeSelection() {
    this.generationSettings.model.projections = 'NONE';
    if(this.generationSettings.model.storageType === 'JOURNAL') {
      this.generationSettings.model.useCQRS = true;
    } else {
      this.generationSettings.model.useCQRS = false;
    }
    this.loadDatabaseTypes();
  }

  onCQRSClick($event) {
    this.generationSettings.model.useCQRS = $event.target.checked;
  }

  move(navigationDirection: NavigationDirection) {
    this.stepCompletion.emit(new StepCompletion(
      Step.AGGREGATE,
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

  storageTypes() {
    return [
      {name: "State Store", value: "STATE_STORE"},
      {name: "Journal", value: "JOURNAL"}
    ];
  }

  loadDatabaseTypes() {
    this.databaseTypes = [
      {name: "In Memory", value: "IN_MEMORY"},
      {name: "Postgres", value: "POSTGRES"},
      {name: "HSQLDB", value: "HSQLDB"},
      {name: "MySQL", value: "MYSQL"},
      {name: "YugaByte", value: "YUGA_BYTE"}
    ];
  }

  projectionOptions() {
    if(this.generationSettings.model.storageType === 'JOURNAL') {
      return [
        {name: "Not Applicable", value: "NONE"},
        {name: "Event Based", value: "EVENT_BASED"}
      ];
    }
    return [
      {name: "Not Applicable", value: "NONE"},
      {name: "Event Based", value: "EVENT_BASED"},
      {name: "Operation Based", value: "OPERATION_BASED"}
    ];
  }

  useCQRS() {
    return this.generationSettings.model.useCQRS;
  }

  shouldDisplayCQRS() {
    return this.generationSettings.model.storageType === 'STATE_STORE';
  }

  private setupRestResources() {
    this.restResourcesSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 2
    };
  }

}
