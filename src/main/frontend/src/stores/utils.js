import { writable } from 'svelte/store';

function parseIfJson(val){
	if (typeof val === 'string' && val.length > 0 && (val.charAt(0) === '{' || val.charAt(0) === '[')){
		return JSON.parse(val);
	}else{
		return val;
	}
}

export function createLocalStore(key, initialValue) {
	const localValue = process.browser && localStorage.getItem(key) ? parseIfJson(localStorage.getItem(key)) : initialValue;
	const { subscribe, set } = writable(localValue);

	return {
		subscribe,
		set: (value) => {
			if (process.browser) {
				localStorage.setItem(key, JSON.stringify(value));
			}
			set(value)
		},
	};
}

export function isMobileStore() {
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
