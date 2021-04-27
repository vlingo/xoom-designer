import { writable } from 'svelte/store';
import { isMobileStore, createLocalStore } from './utils';

export let importedSettings = writable();
export const contextSettings = writable(getLocalStorage("contextSettings"));
export const currentAggregate = writable(getLocalStorage("currentAggregate"));
export const aggregateSettings = writable(getLocalStorage("aggregateSettings") || []);
export const deploymentSettings = writable(getLocalStorage("deploymentSettings"));

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

export const persistenceSettingsInitialValue = {
	storageType: 'STATE_STORE',
	useCQRS: false,
	projections: 'NONE',
	database: 'IN_MEMORY',
	commandModelDatabase: 'IN_MEMORY',
	queryModelDatabase: 'IN_MEMORY',
};

export const isMobile = isMobileStore();
export const theme = createLocalStore('theme', 'light')
export const valueObjectTypes = createLocalStore('valueObjectTypes', [])
export const valueObjectSettings = createLocalStore('valueObjectSettings', [])
export const persistenceSettings = createLocalStore('persistenceSettings', persistenceSettingsInitialValue);
export const simpleTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char'];
export const settingsInfo = createLocalStore('settingsInfo', {});
export const projectGenerationIndex = createLocalStore('projectGenerationIndex', 1);
export const generatedProjectsPaths = createLocalStore('generatedProjectsPaths', []);
export const generationSettings = createLocalStore('generationSettings', {
	useAnnotations: true,
	useAutoDispatch: true
});

export const EDITION_STATUS = {
	NEW: "new",
	CHANGED: "changed"
};

export const STEP_STATUS = {
	CONTEXT: "contextSettingsStatus",
	PERSISTENCE: "persistenceSettingsStatus",
	DEPLOYMENT: "deploymentSettingsStatus",
	GENERATION: "generationSettingsStatus"
}

export function onContextSettingsChange() {
	setLocalStorage(STEP_STATUS.CONTEXT, EDITION_STATUS.CHANGED);
}
export function isContextSettingsChanged() {
	return isChanged(STEP_STATUS.CONTEXT);
}
export function onPersistenceSettingsChange() {
	setLocalStorage(STEP_STATUS.PERSISTENCE, EDITION_STATUS.CHANGED);
}
export function isPersistenceSettingsChanged() {
	return isChanged(STEP_STATUS.PERSISTENCE);
}
export function onDeploymentSettingsChange() {
	setLocalStorage(STEP_STATUS.DEPLOYMENT, EDITION_STATUS.CHANGED);
}
export function isDeploymentSettingsChanged() {
	return isChanged(STEP_STATUS.DEPLOYMENT);
}
export function onGenerationSettingsChange() {
	setLocalStorage(STEP_STATUS.GENERATION, EDITION_STATUS.CHANGED);
}
export function isGenerationSettingsChanged() {
	return isChanged(STEP_STATUS.GENERATION);
}

function isChanged(key) {
	let status = getLocalStorage(key);
	if(!status) {
		return EDITION_STATUS.NEW;
	}
	return status === EDITION_STATUS.CHANGED;
}

export async function reset() {
	localStorage.clear();
}

export async function clearStatuses() {
	for (let key of Object.keys(STEP_STATUS)) {
		setLocalStorage(STEP_STATUS[key], EDITION_STATUS.NEW);
	}
}

