import { writable } from 'svelte/store';
import { defaultContext } from './context';
import { isMobileStore, createLocalStore } from './utils';
import { defaultPersistenceSettings } from './persistence';
import { defaultSettings, defaultGenerationSettings } from './generation';
import { defaultDeploymentSettings } from './deployment';
import Validation from '../util/Validation';

export const isMobile = isMobileStore();
export const updatedSettings = writable();
export const collectionTypes = [{name: "Set"}, {name: "List"}];
export const theme = createLocalStore('theme', 'light');
export const settingsInfo = createLocalStore('settingsInfo', {});
export const EDITION_STATUS = { NEW: "new", CHANGED: "changed" };
export const valueObjectTypes = createLocalStore('valueObjectTypes', [])
export const currentAggregate = writable(getLocalStorage("currentAggregate"));
export const projectGenerationIndex = createLocalStore('projectGenerationIndex', 1);
export const generatedProjectsPaths = createLocalStore('generatedProjectsPaths', []);
export const simpleTypes = ['int', 'double', 'String', 'float', 'short', 'byte', 'boolean', 'long', 'char', 'Date', 'DateTime'];
export const settings = createWritable('settings', defaultSettings, onSettingsChange);

export const platformSettings = writable(getLocalStorage("platformSettings"));

export function updateSettings(newSettings) {
	let emptySettings = {context: {}, model: {persistenceSettings: {}}, deployment: {}};
	updateContext(emptySettings, newSettings);
	updateAggregates(emptySettings, newSettings);
	updatePersistence(emptySettings, newSettings);
	updateDeployment(emptySettings, newSettings);
	updateGeneration(emptySettings, newSettings);
	settings.set(emptySettings);
}

function updateContext(currentSettings, updatedSettings) {
	let updatedContext = updatedSettings.context ? updatedSettings.context : defaultContext;
	currentSettings.context = { ...currentSettings.context, ...updatedContext };
}

function updateAggregates(currentSettings, updatedSettings) {
	if(!updatedSettings.model ||
		!updatedSettings.model.aggregateSettings ||
		!updatedSettings.model.aggregateSettings.length) {
		currentSettings.model = {
			...currentSettings.model,
			aggregateSettings: [],
			valueObjectSettings: []
		}
	} else {
		currentSettings.model = { ...currentSettings.model, ...updatedSettings.model }
	}
}

function updatePersistence(currentSettings, updatedSettings) {
	let importedPersistenceSettings;
	if(!updatedSettings.model || !updatedSettings.model.persistenceSettings) {
		importedPersistenceSettings = defaultPersistenceSettings;
	}  else {
		importedPersistenceSettings = updatedSettings.model.persistenceSettings;
	}currentSettings.model.persistenceSettings = {
		...currentSettings.model.persistenceSettings,
		...importedPersistenceSettings
	}
}

function updateDeployment(currentSettings, updatedSettings) {
	updatedSettings = updatedSettings ? updatedSettings : defaultDeploymentSettings;
	currentSettings.deployment = { ...currentSettings.deployment, ...updatedSettings.deployment }
}

function updateGeneration(currentSettings, updatedSettings) {
	currentSettings.useAnnotations = updatedSettings.useAnnotations != undefined ? updatedSettings.useAnnotations : defaultGenerationSettings.useAnnotations;
	currentSettings.useAutoDispatch = updatedSettings.useAutoDispatch != undefined ? updatedSettings.useAutoDispatch : defaultGenerationSettings.useAutoDispatch;
	currentSettings.projectDirectory = updatedSettings.projectDirectory;
}

export function isSettingsComplete(currentSettings) {
	return currentSettings.context && currentSettings.context.groupId && currentSettings.context.artifactId &&
			currentSettings.context.packageName && currentSettings.context.artifactVersion &&
			Validation.validateContext(currentSettings) && currentSettings.model &&
			currentSettings.model.aggregateSettings && currentSettings.model.aggregateSettings.length > 0 &&
			currentSettings.model.persistenceSettings && Validation.validateDeployment(currentSettings)
			&& currentSettings.projectDirectory;
}

export function isSettingsEdited() {
	return getLocalStorage("settingsEditionStatus") === EDITION_STATUS.CHANGED;
}

export async function clearSettings() {
	updateSettings(defaultSettings);
	setLocalStorage("settingsEditionStatus", EDITION_STATUS.NEW);
}

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

function createWritable(key, value, subscriber) {
	const newWritable = writable(resolveLocalStorage(key, value));
	newWritable.subscribe(subscriber);
	return newWritable;
}

function resolveLocalStorage(key, value) {
	if(process.browser) {
		let stored = JSON.parse(localStorage.getItem(key));
		if(stored) {
			return stored;
		}
		setLocalStorage(key, value);
		return value;
	}
	return undefined;
}

export function onSettingsChange(changedSettings) {
	setLocalStorage('settings', changedSettings);
	setLocalStorage("settingsEditionStatus", EDITION_STATUS.CHANGED)
}

export const isValid = writable({
	context: false,
	aggregates: false,
	deployment: false,
})
