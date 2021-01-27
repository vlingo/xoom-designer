import { writable } from 'svelte/store';

export function createLocalStore(key, initialValue) {
	const localValue = process.browser ? localStorage.getItem(key) : initialValue;
	const { subscribe, set } = writable(localValue);

	return {
		subscribe,
		set: (value) => {
			if (process.browser) {
				localStorage.setItem(key, value);
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