import { readable } from 'svelte/store';

export const deploymentTypes = readable(null, function start(set) {
	set([
    { label: "Default", value: "NONE" },
		{ label: "Docker", value: "DOCKER" },
		{ label: "Kubernetes", value: "KUBERNETES" },
  ]);
});

export const defaultDeploymentSettings = {
    "type": "NONE",
    "dockerImage": "",
    "kubernetesImage": "",
    "kubernetesPod": "",
    "clusterTotalNodes": 3,
    "clusterPort": 50011,
    "producerExchangePort": 19762,
    "httpServerPort": 8081,
    "pullSchemas": false
}

