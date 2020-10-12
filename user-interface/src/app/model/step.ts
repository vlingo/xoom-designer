export enum Step {
    CONTEXT,
    AGGREGATE,
    PERSISTENCE,
    DEPLOYMENT,
    GENERATION
}

export const StepPath = new Map<number, string>([
    [Step.CONTEXT, '/settings/context'],
    [Step.AGGREGATE, '/settings/model/aggregate'],
    [Step.PERSISTENCE, '/settings/model/persistence'],
    [Step.DEPLOYMENT, '/settings/deployment'],
    [Step.GENERATION, '/settings/generation']
]);

export const StepName = new Map<number, string>([
    [Step.CONTEXT, 'Context'],
    [Step.AGGREGATE, 'Model'],
    [Step.PERSISTENCE, 'Persistence'],
    [Step.DEPLOYMENT, 'Deployment'],
    [Step.GENERATION, 'Generate']
]);
