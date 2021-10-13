import Repository from './Repository'

const resources = Object.freeze({
	projectGeneration: '/model-processing',
	exportationFile: '/model-processing/exportation-file',
	importationFile: '/model-processing/importation-file',
	pathValidation: '/model-processing/paths',
	generationInfo: '/model-processing/info',
})

function ensureOk(response) {
	return ensure(response, 200);
}

function ensure(response, status) {
	if(response.status === 500) {
		return response.json().then(json => Promise.reject(json));
	}
	if (response.status === 429) {
		throw Error("Your request could not be processed. Please try again later.");
	}
	if (response.status !== status) {
		throw Error(`HTTP ${response.status}: ${response.statusText}.`);
	}
	return response;
}

export default {
	generateProject(settings) {
		return Repository.post(resources.projectGeneration, settings).then(ensureOk).then(res => res.json());
	},

	downloadExportationFile(settings) {
		return Repository.post(resources.exportationFile, settings).then(ensureOk).then(res => res.json());
	},

	processImportFile(encodedFile) {
		return Repository.post(resources.importationFile, {"encoded": encodedFile}).then(ensureOk).then(res => res.json());
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
