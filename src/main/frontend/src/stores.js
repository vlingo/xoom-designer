import { writable } from 'svelte/store';

export const theme = writable("light");
export const mobileStore = writable(false);

export const contextSettings = writable(getLocalStorage("contextSettings"));
export const currentAggregate = writable(getLocalStorage("currentAggregate"));
export const aggregateSettings = writable(getLocalStorage("aggregateSettings") || []);
export const persistenceSettings = writable(getLocalStorage("persistenceSettings"));
export const deploymentSettings = writable(getLocalStorage("deploymentSettings"));
export const generationSettings = writable(getLocalStorage("generationSettings"));

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