import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { LeftMenuComponent } from './left-menu/left-menu.component';
import { ContextComponent } from './context/context.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LeftMenuComponent,
    ContextComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
