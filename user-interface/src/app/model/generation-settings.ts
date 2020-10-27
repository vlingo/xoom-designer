import { Model } from './model-aggregate';
import { ContextSettings } from './context-settings';
import { DeploymentSettings } from './deployment-settings';
import { DomainEvent } from './domain-event';
import { Aggregate } from './aggregate';

export interface GenerationSettings {
    context: ContextSettings;
    model: Model;
    deployment: DeploymentSettings;
    projectDirectory: String;
    useAnnotations: Boolean;
    useAutoDispatch: Boolean;
}
