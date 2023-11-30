import {artifactRule, namespaceRule, packageRule, projectNameRule, versionRule} from '../validators';

export default {
    validateContext(settings) {
        return settings && settings.context && !packageRule(settings.context.groupId) && !artifactRule(settings.context.artifactId) && 
        !versionRule(settings.context.artifactVersion) && !packageRule(settings.context.packageName)
    },
    validateDeployment(settings) {
        let clusterNodesValidity = settings.deployment.clusterTotalNodes >= 0;
        let kubernetesValidity = settings.deployment.kubernetesImage && settings.deployment.kubernetesPod;
        let deploymentTypeValidity = (settings.deployment.type === "NONE" ? true : settings.deployment.dockerImage && (settings.deployment.type === "DOCKER" ? true : kubernetesValidity));
        return clusterNodesValidity && settings.deployment.type && deploymentTypeValidity;		
    },
    validateAggregates({ model: { aggregateSettings: aggregates }}) {
        return aggregates && aggregates.length > 0;
    },
    validatePlatformWithContext(settings, context) {
        return settings && settings.context && !projectNameRule(context.solutionName) && !projectNameRule(context.projectName)
          && !versionRule(settings.context.projectVersion) && !namespaceRule(settings.context.namespace);
    }
}