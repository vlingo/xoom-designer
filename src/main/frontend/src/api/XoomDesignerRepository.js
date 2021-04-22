import Repository from './Repository'

const resources = Object.freeze({
	projectGeneration: '/generation-settings',
	exportationFile: '/generation-settings/exportation-file',
	importationFile: '/generation-settings/importation-file',
	pathValidation: '/generation-settings/paths',
	generationInfo: '/generation-settings/info',
})

function ensureOk(response) {
	return ensure(response, 200);
}

function ensure(response, status) {
	if(response.status === 500) {
		return response.json().then(json => Promise.reject(json));
	}
	if (response.status !== status) {
		throw Error(`HTTP ${response.status}: ${response.statusText}.`);
	}
	return response;
}


export default {
	generateProject(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch) {
		return Repository.post(resources.projectGeneration, {
			context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch
		}).then(ensureOk).then(res => res.json());
	},

	downloadExportationFile(context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch) {
		return repoPoRepository.post(resources.exportationFile, {
			context, model, deployment, projectDirectory, useAnnotations, useAutoDispatch
		}).then(ensureOk).then(res => res.json());
	},

	processImportFile(encodedFile) {
		return Repository.post(resources.importationFile, {
			"encoded": encodedFile
		}).then(ensureOk).then(res => res.json());
	},

	checkPath(projectDirectory) {
		return Repository.post(resources.pathValidation, {
			path: projectDirectory
		});
	},

	queryInfo() {
		return Repository.get(resources.generationInfo)
			.then(ensureOk).then(res => res.json());
	}
}
