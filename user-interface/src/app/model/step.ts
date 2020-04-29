export enum Step {
    CONTEXT,
    MODEL,
    DEPLOYMENT,
    GENERATION
}

export const StepPath = new Map<number, string>([
    [Step.CONTEXT, '/settings/context'],
    [Step.MODEL, '/settings/model'],
    [Step.DEPLOYMENT, '/settings/deployment'],
    [Step.GENERATION, '/settings/generation']
]);

export const StepName = new Map<number, string>([
    [Step.CONTEXT, 'Context'],
    [Step.MODEL, 'Model'],
    [Step.DEPLOYMENT, 'Deployment'],
    [Step.GENERATION, 'Generate']
]); 