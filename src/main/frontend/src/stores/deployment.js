import { readable } from 'svelte/store';

export const deploymentTypes = readable(null, function start(set) {
	set([
    { label: "Default", value: "NONE" },
		{ label: "Docker", value: "DOCKER" },
		{ label: "Kubernetes", value: "KUBERNETES" },
  ]);
});

export const defaultDeploymentSettings = {
	type: "NONE",
	clusterNodes: 3,
	dockerImage: "",
	kubernetesImage: "",
	kubernetesPod: ""
}