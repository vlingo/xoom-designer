import { PersistenceComponent } from './steps/model/persistence/persistence.component';
import { AggregatesSettingsComponent } from './steps/model/aggregates-settings/aggregates-settings.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContextComponent } from './steps/context/context.component';
import { DeploymentComponent } from './steps/deployment/deployment.component';
import { GenerationComponent } from './steps/generation/generation.component';
import { AboutComponent } from './about/about.component';
import { SettingsComponent } from './settings/settings.component';

const routes: Routes = [
  {
    path: 'settings',
    component: SettingsComponent,
    children: [
      {
        path: 'context',
        component: ContextComponent
      },
      {
        path: 'model/aggregate',
        component: AggregatesSettingsComponent
      },
      {
        path: 'model/persistence',
        component: PersistenceComponent
      },
      {
        path: 'deployment',
        component: DeploymentComponent
      },
      {
        path: 'generation',
        component: GenerationComponent
      }
    ]
  },
  {
    path: 'about',
    component: AboutComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
