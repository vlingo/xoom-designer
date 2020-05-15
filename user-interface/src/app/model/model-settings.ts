import { Aggregate } from './aggregate';
import { DomainEvent } from './domain-event';

export class ModelSettings {
    
    public aggregates = new Array<Aggregate>();
    public restResources = new Array<Aggregate>();
    public storageType: String;
    public useCQRS: Boolean;
    public useProjections: Boolean;
    public database: String;
    public commandModelDatabase: String;
    public queryModelDatabase: String;

    constructor() {
        this.storageType = "STATE_STORE";
        this.database = "IN_MEMORY";
        this.commandModelDatabase ="IN_MEMORY";
        this.queryModelDatabase = "IN_MEMORY";
    }

    public domainEventsOf(aggregateId: Number) : Array<DomainEvent> {
        const aggregate = this.aggregateOf(aggregateId);

        if(aggregate == undefined) {
            return new Array<DomainEvent>();
        }

        return aggregate.events;
    }

    public aggregateOf(aggregateId: Number) : Aggregate {
        return this.aggregates.find(aggregate => aggregateId === aggregate.id);
    }

}