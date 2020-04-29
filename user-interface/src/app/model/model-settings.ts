import { Aggregate } from './aggregate';
import { DomainEvent } from './domain-event';

export class ModelSettings {

    
    public aggregates = new Array<Aggregate>();
    public restResources = new Array<Aggregate>();
    public storageType: String;
    public useCQRS: Boolean;

    constructor() {
        this.storageType = "STATE_STORE";
    }

    public domainEventsOf(aggregateId: Number) : Array<DomainEvent> {
        const aggregate = this.aggregateOfId(aggregateId);

        if(aggregate == undefined) {
            return new Array<DomainEvent>();
        }

        return aggregate.events;
    }

    public aggregateOfId(aggregateId: Number) : Aggregate {
        return this.aggregates.find(aggregate => aggregateId === aggregate.id);
    }

}