import { StepCompletion } from '../model/step-completion'
import { Output, OnInit, EventEmitter, Input } from '@angular/core';
import { GenerationSettingsModel } from '../model/generation-settings.model';

export abstract class StepComponent implements OnInit {
    
    @Input() generation: GenerationSettingsModel;
    @Output() stepCompletion = new EventEmitter<StepCompletion>();
    
    abstract ngOnInit();
    abstract next(): void;
    abstract previous(): void;
    abstract hasNext() : Boolean;
    abstract hasPrevious() : Boolean;

    public generate() {
        throw new Error("This operation is not supported by default");
    }

    public isLastStep() :  Boolean {
        return false;
    }

}