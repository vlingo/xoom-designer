import {
  Persistence,
  Model
} from './../../../model/model-aggregate';
import {
  SettingsStepService
} from '../../../service/settings-step.service';
import {
  FormGroup,
  FormBuilder,
  Validators
} from '@angular/forms';
import {
  NavigationDirection
} from 'src/app/model/navigation-direction';
import {
  Step
} from 'src/app/model/step';
import {
  Component,
  OnInit
} from '@angular/core';
import {
  StepComponent
} from '../../step.component';
import {
  StepCompletion
} from 'src/app/model/step-completion';

@Component({
  selector: 'app-persistence',
  templateUrl: './persistence.component.html',
  styleUrls: ['./persistence.component.css']
})
export class PersistenceComponent extends StepComponent implements OnInit {

  persistenceForm: FormGroup;
  projections;
  storageTypes = [{
      name: 'State Store',
      value: 'STATE_STORE'
    },
    {
      name: 'Journal',
      value: 'JOURNAL'
    }
  ];
  databases = [{
      name: 'In Memory',
      value: 'IN_MEMORY'
    },
    {
      name: 'Postgres',
      value: 'POSTGRES'
    },
    {
      name: 'HSQLDB',
      value: 'HSQLDB'
    },
    {
      name: 'MySQL',
      value: 'MYSQL'
    },
    {
      name: 'YugaByte',
      value: 'YUGA_BYTE'
    }
  ];


  constructor(private formBuilder: FormBuilder, private settingsStepService: SettingsStepService) {
    super();
    settingsStepService.getSettings$.subscribe(settings => {
      this.createNewForm(settings.model);
    });
  }

  ngOnInit(): void {
    this.persistenceForm.get('storageType').valueChanges.subscribe(value => {
      this.defineProjections(value);
    });
  }

  createNewForm(model: Model) {
    const persistence = (model && model.persistence) ? model.persistence : {} as Persistence;
    this.persistenceForm = this.formBuilder.group({
      storageType: [persistence.storageType, [Validators.required]],
      useCQRS: [persistence.useCQRS, [Validators.required]],
      projections: [persistence.projections, [Validators.required]],
      database: [persistence.database, []],
      commandModelDatabase: [persistence.commandModelDatabase, []],
      queryModelDatabase: [persistence.queryModelDatabase, []],
    });
    this.defineProjections(persistence.storageType);
  }

  next(): void {
    const persistence = this.persistenceForm.value;
    this.settingsStepService.addPersistence(persistence);
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

  private defineProjections(storageType: string){
    this.projections = (storageType === 'JOURNAL') ? [{
      name: 'Not Applicable',
      value: 'NONE'
    },
    {
      name: 'Event Based',
      value: 'EVENT_BASED'
    }
  ] :
  [{
      name: 'Not Applicable',
      value: 'NONE'
    },
    {
      name: 'Event Based',
      value: 'EVENT_BASED'
    },
    {
      name: 'Operation Based',
      value: 'OPERATION_BASED'
    }
  ];
  }

}
