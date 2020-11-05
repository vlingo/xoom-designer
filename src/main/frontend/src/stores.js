import { writable } from 'svelte/store';

export const theme = writable("light");
export const mobileStore = writable(false);

export const contextSettings = writable(undefined);
// export const modelSettings = writable(undefined);
export const aggregateSettings = writable(undefined);
export const persistenceSettings = writable(undefined);
export const deploymentSettings = writable(undefined);
export const generationSettings = writable(undefined);