export class DomainEvent {

    constructor(public id: Number, public name:String) {
        this.name = this.name.trim();
    }

}