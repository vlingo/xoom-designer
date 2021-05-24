import { writable } from 'svelte/store';

export function isJson(str) {
  try {
      JSON.parse(str);
  } catch (e) {
      return false;
  }
  return true;
}

function parseIfJson(val){
	if (isJson(val)){
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
