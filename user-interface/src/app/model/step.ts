export enum Step {
    CONTEXT,
    MODEL,
    DEPLOYMENT,
    GENERATION
}

export const StepPath = new Map<number, string>([
    [Step.CONTEXT, '/context'],
    [Step.MODEL, '/model'],
    [Step.DEPLOYMENT, '/deployment'],
    [Step.GENERATION, '/generation']
]);