import { SettingsStepService } from './../../service/settings-step.service';
import { DeploymentSettings } from './../../model/deployment-settings';
import { Component, OnInit } from '@angular/core';
import { StepComponent } from '../step.component';
import { NavigationDirection } from 'src/app/model/navigation-direction';
import { StepCompletion } from 'src/app/model/step-completion';
import { Step } from 'src/app/model/step';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-deployment',
  templateUrl: './deployment.component.html',
  styleUrls: ['./deployment.component.css']
})
export class DeploymentComponent extends StepComponent {

  deploymentForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private settingsStepService: SettingsStepService) {
    super();
    this.createForm();
  }

  ngOnInit(): void {
    this.deploymentForm.get('deploymentType')
    .setValue(this.generationSettings.deployment.type);
  }

  createForm() {
    this.deploymentForm = this.formBuilder.group({
      clusterNodes: ['', Validators.required],
      deploymentType: ['NONE', Validators.required],
      dockerImage: [''],
      kubernetesImage: [''],
      kubernetesPOD: ['']
    });

    const dockerImage = this.deploymentForm.get('dockerImage');
    const kubernetesImage = this.deploymentForm.get('kubernetesImage');
    const kubernetesPOD = this.deploymentForm.get('kubernetesPOD');

    this.deploymentForm.get('deploymentType').valueChanges
      .subscribe(deploymentType => {
        if(deploymentType === 'NONE') {
          dockerImage.setValidators(null);
          kubernetesImage.setValidators(null);
          kubernetesPOD.setValidators(null);
        }

        if(deploymentType === 'DOCKER') {
          dockerImage.setValidators([Validators.required]);
          kubernetesImage.setValidators(null);
          kubernetesPOD.setValidators(null);
        }

        if(deploymentType === 'KUBERNETES') {
          dockerImage.setValidators([Validators.required]);
          kubernetesImage.setValidators([Validators.required]);
          kubernetesPOD.setValidators([Validators.required]);
        }

        dockerImage.updateValueAndValidity();
        kubernetesPOD.updateValueAndValidity();
        kubernetesImage.updateValueAndValidity();
        this.generationSettings.deployment.type = deploymentType;
      });
  }

  next() {
    const deployment = this.deploymentForm.value as DeploymentSettings;
    this.settingsStepService.addDeployment(deployment);
    this.move(NavigationDirection.FORWARD);
  }

  previous() {
    this.move(NavigationDirection.REWIND);
  }

  isDeploymentTypeSelected() : Boolean {
    return this.generationSettings.deployment.type !== 'NONE';
  }

  isKubernetesSelected() : Boolean {
    return this.generationSettings.deployment.type === 'KUBERNETES';
  }

  private move(navigationDirection: NavigationDirection) {
    this.stepCompletion.emit(new StepCompletion(
      Step.DEPLOYMENT,
      this.deploymentForm.valid,
      navigationDirection
    ));
  }

  hasNext(): Boolean {
    return true;
  }

  hasPrevious(): Boolean {
    return true;
  }

  canFinish(): Boolean {
    return false;
  }

}
