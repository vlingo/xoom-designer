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
    parameters: string[];
    event: string;
  }

  export interface Route {
    path: string;
    httpMethod: string;
    aggregateMethod: string;
    requireEntityLoad: string;
  }

  export interface Api {
    rootPath: string;
    routes: Route[];
  }

  export interface AggregatesSetting {
    aggregateName: string;
    stateFields: StateField[];
    events: Event[];
    methods: Method[];
    api: Api;
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

  export interface RootObject {
    model: Model;
  }
