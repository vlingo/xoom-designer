import { ModelService } from './../model.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { Step } from 'src/app/model/step';
import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../../step.component';
import { StepCompletion } from 'src/app/model/step-completion';

@Component({
  selector: 'app-persistence',
  templateUrl: './persistence.component.html',
  styleUrls: ['./persistence.component.css']
})
export class PersistenceComponent extends StepComponent implements OnInit {

  persistenceForm: FormGroup;
  storageTypes = ['STATE_STORE'];
  projections = ['EVENT_BASED'];
  commandModelDatabases = ['POSTGRES'];
  queryModelDatabases = ['MYSQL'];


  constructor(private formBuilder: FormBuilder, private modelService: ModelService) {
    super();
    this.createNewForm();
  }

  ngOnInit(): void {
  }

  createNewForm(){
    this.persistenceForm = this.formBuilder.group({
      storageType: ['', [Validators.required]],
      useCqrs: [false, [Validators.required]],
      projections: ['', [Validators.required]],
      database: ['', [Validators.required]],
      commandModelDatabase: ['', [Validators.required]],
      queryModelDatabase: ['', [Validators.required]],
    });
}

next(): void {
    this.modelService.addPersistence(this.persistenceForm.value);
    this.stepCompletion.emit(new StepCompletion(
      Step.PERSISTENCE,
      true,
      NavigationDirection.FORWARD
    ));
  }

  previous(): void {
    this.stepCompletion.emit(new StepCompletion(
      Step.PERSISTENCE,
      true,
      NavigationDirection.REWIND
    ));
  }

  hasNext(): Boolean {
    return true;
  }
  hasPrevious(): Boolean {
    return true;
  }

}
