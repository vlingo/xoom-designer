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

  constructor(private formBuilder: FormBuilder) { 
    super();
    this.createForm();
  }

  ngOnInit(): void {
    this.deploymentForm.get("DeploymentType")
    .setValue(this.generationSettings.deployment.type);
  }
  
  createForm() {
    this.deploymentForm = this.formBuilder.group({
      ClusterNodes: ['', Validators.required],
      DeploymentType: ['NONE', Validators.required],
      DockerImage: [''],
      KubernetesImage: [''],
      KubernetesPOD: ['']
    });

    const dockerImage = this.deploymentForm.get('DockerImage');
    const kubernetesImage = this.deploymentForm.get('KubernetesImage');
    const kubernetesPOD = this.deploymentForm.get('KubernetesPOD');

    this.deploymentForm.get("DeploymentType").valueChanges
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
    this.move(NavigationDirection.FORWARD);
  }  

  previous() {
    this.move(NavigationDirection.REWIND);
  }

  isDeploymentTypeSelected() : Boolean {
    return this.generationSettings.deployment.type != "NONE";
  }

  isKubernetesSelected() : Boolean {
    return this.generationSettings.deployment.type === "KUBERNETES";
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
