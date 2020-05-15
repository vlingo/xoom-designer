import { DomainEvent } from './domain-event';

export class Aggregate {
    public events = new Array<DomainEvent>();
    
    constructor(public id: Number,public name: String) {
        this.name = this.name.trim();
    }

}