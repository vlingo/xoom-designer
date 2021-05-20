import { defaultContext } from './context';
import { defaultDeploymentSettings } from './deployment';
import { defaultPersistenceSettings } from './persistence';

export const TARGET = {ZIP_DOWNLOAD: "zip-download", FILESYSTEM : "filesystem"};
export const defaultGenerationSettings = {useAnnotations: true, useAutoDispatch: true};
export const defaultSettings = { context: defaultContext, 
	deployment: defaultDeploymentSettings, 
	model: { 
		aggregateSettings: [], 
		valueObjectSettings: [], 
		persistenceSettings: defaultPersistenceSettings 
	}, 
	useAnnotations: defaultGenerationSettings.useAnnotations, 
	useAutoDispatch: defaultGenerationSettings.useAutoDispatch,
	projectDirectory: defaultGenerationSettings.projectDirectory
};

