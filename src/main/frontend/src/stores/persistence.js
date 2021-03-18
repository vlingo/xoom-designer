import { readable, writable } from 'svelte/store';

export const storageTypes = readable(null, function start(set) {
	set([
    {name: "State Store for Key-Value Persistence", value: "STATE_STORE"},
    {name: "Journal for Event Sourcing", value: "JOURNAL"}
  ]);
});

export const projectionsTypes = writable([
  {name: 'Not Applicable', value: 'NONE'},
  {name: 'Event-based', value: 'EVENT_BASED'},
  {name: 'Operation-based', value: 'OPERATION_BASED'}
]);

export const databaseTypes = readable(null, function start(set) {
	set([
    {name: "In Memory", value: "IN_MEMORY"},
    {name: "Postgres", value: "POSTGRES"},
    {name: "HSQLDB", value: "HSQLDB"},
    {name: "MySQL", value: "MYSQL"},
    {name: "YugaByte", value: "YUGA_BYTE"}
  ]);
});
