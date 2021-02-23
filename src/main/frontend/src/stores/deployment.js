import { readable } from 'svelte/store';
export const deploymentTypes = readable(null, function start(set) {
	set([
    { label: "None", value: "NONE" },
		{ label: "Docker", value: "DOCKER" },
		{ label: "Kubernetes", value: "KUBERNETES" },
  ]);
});
