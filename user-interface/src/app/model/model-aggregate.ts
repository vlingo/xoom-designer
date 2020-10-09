  export interface StateField {
    name: string;
    type: string;
  }

  export interface Event {
    name: string;
    fields: string[];
  }

  export interface Method {
    name: string;
    factory: string;
    params: string[];
    events: Event[];
  }

  export interface Route {
    route: string;
    httpMethod: string;
    aggregateMethod: string;
  }

  export interface AggregatesSetting {
    aggregateName: string;
    stateFields: StateField[];
    methods: Method[];
    routes: Route[];
  }

  export interface Persistence {
    storageType: string;
    useCQRS: string;
    projections: string;
    database: string;
    commandModelDatabase: string;
    queryModelDatabase: string;
  }

  export interface Model {
    aggregatesSettings: AggregatesSetting[];
    persistence: Persistence;
  }

  export interface ModelAggregate {
    model: Model;
  }
