import { defaultContext } from './context';
import { defaultDeploymentSettings } from './deployment';
import { defaultPersistenceSettings } from './persistence';
import { defaultSchemataSettings } from './schemata';

export const TARGET = {ZIP_DOWNLOAD: "zip-download", FILESYSTEM : "filesystem"};
export const defaultGenerationSettings = {useAnnotations: true, useAutoDispatch: true};
export const defaultPlatformSettings = {
	platform: "JVM",
	lang: ["Java"]
}
export const defaultSettings = { context: defaultContext, 
	deployment: defaultDeploymentSettings, 
	model: { 
		aggregateSettings: [], 
		valueObjectSettings: [], 
		persistenceSettings: defaultPersistenceSettings 
	}, 
	schemata: defaultSchemataSettings,
	useAnnotations: defaultGenerationSettings.useAnnotations, 
	useAutoDispatch: defaultGenerationSettings.useAutoDispatch,
	projectDirectory: defaultGenerationSettings.projectDirectory,
	platformSettings: { ...defaultGenerationSettings }
};

