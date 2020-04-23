import { ContextModel } from './context.model';
import { ModelModel } from './model-settings.model';
import { DeploymentSettingsModel } from './deployment-settings.model';

export class GenerationSettingsModel {

    context: ContextModel;
    model: ModelModel;
    deployment: DeploymentSettingsModel;

    constructor() {
        this.model = new ModelModel();
        this.context = new ContextModel();
        this.deployment = new DeploymentSettingsModel();
    }
}