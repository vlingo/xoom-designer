import { ContextSettings } from './context-settings';
import { ModelSettings } from './model-settings';
import { DeploymentSettings } from './deployment-settings';
import { DomainEvent } from './domain-event';
import { Aggregate } from './aggregate';

export class GenerationSettings {


    public context: ContextSettings;
    public model: ModelSettings;
    public deployment: DeploymentSettings;
    public projectDirectory: String;
    
    constructor() {
        this.model = new ModelSettings();
        this.context = new ContextSettings();
        this.deployment = new DeploymentSettings();
    }
    
    public addAggregate(aggregate: Aggregate) {
      this.model.aggregates.push(aggregate);
    }

    public addRestResources(aggregates: Array<Aggregate>) {
        this.model.restResources = [];
        aggregates.forEach(aggregate => {
            this.model.restResources.push(aggregate);
        })
    }

    public addDomainEvent(selectedAggregateId: Number, domainEvent: DomainEvent) {
        this.domainEventsOf(selectedAggregateId).push(domainEvent);
    }
    
    public domainEventsOf(aggregateId: Number) : Array<DomainEvent> {
        return this.model.domainEventsOf(aggregateId);
    }
    
}