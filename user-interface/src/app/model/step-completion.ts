import { Step } from './step';
import { NavigationDirection } from './navigation-direction';

export class StepCompletion {
    
    constructor(
        public step: Step, 
        public valid: Boolean,
        public navigationDirection: NavigationDirection) {
    }
    
    public nextStep() : Step {
        let followingStepIndex = this.isDirectionForward() ? this.step + 1 : this.step - 1; 
        return Step[Step[followingStepIndex]];
    }

    public requiresNavigation(): boolean {
        return this.navigationDirection != NavigationDirection.FINISH;
    }

    public isGenerationRequested(): boolean {
        return this.navigationDirection === NavigationDirection.FINISH;
    }
        
    private isDirectionForward() : boolean {
        return this.navigationDirection === NavigationDirection.FORWARD; 
    }

}