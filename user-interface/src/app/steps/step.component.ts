import { StepCompletion } from '../model/step-completion'
import { Output, OnInit, EventEmitter, Input } from '@angular/core';
import { GenerationSettings } from '../model/generation-settings';

export abstract class StepComponent implements OnInit {
    
    @Input() generationSettings: GenerationSettings;
    @Output() stepCompletion = new EventEmitter<StepCompletion>();
    
    abstract ngOnInit(): void;
    abstract next(): void;
    abstract previous(): void;
    abstract hasNext() : Boolean;
    abstract hasPrevious() : Boolean;

    public generate() {
        throw new Error("Generation is not supported by default");
    }

    public isLastStep() :  Boolean {
        return false;
    }

}