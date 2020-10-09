import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LeftMenuComponent } from './left-menu/left-menu.component';
import { ContextComponent } from './steps/context/context.component';
import { ModelComponent } from './steps/model/model.component';
import { TableComponent } from './steps/model/table/table.component';
import { DeploymentComponent } from './steps/deployment/deployment.component';
import { GenerationComponent } from './steps/generation/generation.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ToastrModule } from 'ngx-toastr';
import { MultipleSelectionComponent } from './multiple-selection/multiple-selection.component';
import { StepTitleComponent } from './steps/step-title/step-title.component';
import { AboutComponent } from './about/about.component';
import { SettingsComponent } from './settings/settings.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider';
import { MatSelectModule} from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { HttpRequestHandler } from './interceptor/http-request-handler';
import { LoaderComponent } from './loader/loader.component';
import { AggregatesSettingsComponent } from './steps/model/aggregates-settings/aggregates-settings.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LeftMenuComponent,
    ContextComponent,
    ModelComponent,
    TableComponent,
    DeploymentComponent,
    GenerationComponent,
    MultipleSelectionComponent,
    StepTitleComponent,
    AboutComponent,
    SettingsComponent,
    LoaderComponent,
    AggregatesSettingsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    NgMultiSelectDropDownModule.forRoot(),
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDividerModule,
    MatSelectModule,
    MatCardModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: HttpRequestHandler,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
