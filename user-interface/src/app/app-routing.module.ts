import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContextComponent } from './context/context.component';


const routes: Routes = [
  {
    path: 'context',
    component: ContextComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
