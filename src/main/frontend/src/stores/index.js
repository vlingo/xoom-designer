import { writable } from 'svelte/store';
<<<<<<< HEAD:src/main/frontend/src/stores/index.js
import { isMobileStore, createLocalStore } from './utils';
=======

export const theme = writable("light");
>>>>>>> mobileStore and breakpoints fixes:src/main/frontend/src/stores.js

export const contextSettings = writable(getLocalStorage("contextSettings"));
export const currentAggregate = writable(getLocalStorage("currentAggregate"));
export const aggregateSettings = writable(getLocalStorage("aggregateSettings") || []);
export const persistenceSettings = writable(getLocalStorage("persistenceSettings"));
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

<<<<<<< HEAD:src/main/frontend/src/stores/index.js
export const isMobile = isMobileStore();
export const theme = createLocalStore('theme', 'light')
=======
function localStorageStore(key, initialValue = {}) {

}

function isMobileStore() {
	const { subscribe, set } = writable(false);
	return {
		subscribe,
		set,
		check: () => {
			import('svelte-materialify/src/utils/breakpoints').then(({ default: breakpoints }) => {
				set(window.matchMedia(breakpoints['md-and-down']).matches);
			});
		},
	};
};

export const isMobile = isMobileStore();
>>>>>>> mobileStore and breakpoints fixes:src/main/frontend/src/stores.js
