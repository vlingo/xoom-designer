import { writable } from 'svelte/store';
import { defaultContext } from './context';
import { isMobileStore, createLocalStore } from './utils';
import { defaultPersistenceSettings } from './persistence';
import { defaultSettings, defaultGenerationSettings } from './generation';
import { defaultDeploymentSettings } from './deployment';
import { defaultSchemataSettings } from './schemata';
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
export const settings = createWritableSettings('settings', defaultSettings, onSettingsChange);
export const schemataData = createLocalStore('schemataData', {
	organizationsStore: [],
	unitsStore: [],
	contextsStore: [],
	schemasStore: [],
	schemaVersionsStore: [],
});

export const valueObjectNameChanges = { 
	oldName:'',
	newName:'',
	listeners: [],
	finalizers: [],
	log: function (oldTypeName, newTypeName) {
		this.oldName = oldTypeName;
		this.newName = newTypeName;
		this.listeners.forEach(listener => listener.handle("CHANGED"));
	},
	addListener(listener) {
		this.listeners.push({handle: listener});
	},
	addFinalizer(finalizer){
		this.finalizers.push({handle: finalizer});
	},
	currentNameOf(fieldType) {
		if(this.oldName == fieldType) {
			return this.newName;
		} 
		return '';
	},
	isDeprecated(fieldType) {
		return this.oldName === fieldType;
	},
	finishWith(settings) {
		if(this.newName) {
			this.listeners.forEach(listener => listener.handle("DONE"));
			if(settings.model) {
				settings.model.aggregateSettings.forEach(aggregate => {
					aggregate.stateFields.forEach(field => {
						if(this.isDeprecated(field.type)) {
							field.type = this.newName;
						}
					});
				});
				settings.model.aggregateSettings = settings.model.aggregateSettings;
			}
			this.finalizers.forEach(finalizer => finalizer.handle());
		}
	},
	deprecatedValues() {
		return [this.oldName];
	}
};

export function importSettings(importedSettings) {
	let updatedSettigns = updateSettings(importedSettings);
	settings.set(updatedSettigns);
}

function updateSettings(newSettings) {
	newSettings = newSettings ? newSettings : defaultSettings;
	let emptySettings = {context: {}, model: {persistenceSettings: {}}, schemata: {}, deployment: {}};
	updateContext(emptySettings, newSettings);
	updateAggregates(emptySettings, newSettings);
	updatePersistence(emptySettings, newSettings);
	updateDeployment(emptySettings, newSettings);
	updateSchemata(emptySettings, newSettings);
	updateGeneration(emptySettings, newSettings);
	return emptySettings;
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
	}
	
	currentSettings.model.persistenceSettings = {
		...currentSettings.model.persistenceSettings,
		...importedPersistenceSettings
	}
}

function updateDeployment(currentSettings, updatedSettings) {
	let updatedDeployment = updatedSettings.deployment ? updatedSettings.deployment : defaultDeploymentSettings;
	currentSettings.deployment = { ...currentSettings.deployment, ...updatedDeployment }
}

function updateSchemata(currentSettings, updatedSettings) {
	let updatedSchemata = updatedSettings.schemata ? updatedSettings.schemata  : defaultSchemataSettings;
	currentSettings.schemata = { ...currentSettings.schemata, ...updatedSchemata }
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
	importSettings(defaultSettings);
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

function createWritableSettings() { 
	const updatedSettings = updateSettings(resolveLocalStorage('settings', defaultSettings));
	const newWritable = writable(updatedSettings);
	newWritable.subscribe(onSettingsChange);
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

export function subscribeToSchemataChanges(subscriber) {
	schemataData.subscribe(subscriber);
}

export const isValid = writable({
	context: false,
	aggregates: false,
	deployment: false,
	contextWithPlatform: false,
})
