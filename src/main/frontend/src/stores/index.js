import { writable } from 'svelte/store';
import { isMobileStore, createLocalStore } from './utils';

export const contextSettings = writable(getLocalStorage("contextSettings"));
export const currentAggregate = writable(getLocalStorage("currentAggregate"));
export const aggregateSettings = writable(getLocalStorage("aggregateSettings") || []);
export const deploymentSettings = writable(getLocalStorage("deploymentSettings"));
export const generationSettings = writable(getLocalStorage("generationSettings"));

/*
* checking process.browser simply means that only run code snippet in client side.
*/

export function getLocalStorage(key) {
	if(process.browser) {
		return JSON.parse(localStorage.getItem(key)) || undefined;
	}
	return undefined;
}

export function setLocalStorage(key, value) {
	if(process.browser) {
		localStorage.setItem(key, JSON.stringify(value));
	}
}

export const isMobile = isMobileStore();
export const theme = createLocalStore('theme', 'light')
export const valueObjectTypes = createLocalStore('valueObjectTypes', [])
export const valueObjectSettings = createLocalStore('valueObjectSettings', [])
export const persistenceSettings = createLocalStore('persistenceSettings', {
	storageType: 'STATE_STORE',
	useCQRS: false,
	projections: 'NONE',
	database: 'IN_MEMORY',
	commandModelDatabase: 'IN_MEMORY',
	queryModelDatabase: 'IN_MEMORY',
})
export const simpleTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char'];
export const settingsInfo = createLocalStore('settingsInfo', {});