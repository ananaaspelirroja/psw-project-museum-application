import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router'; // Importa RouterModule
import { AppComponent } from './app.component';
import {HttpClient, HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [AppComponent], // Usa declarations per componenti non standalone
  imports: [BrowserModule, RouterModule, HttpClientModule], // Aggiungi RouterModule qui
  providers: [HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
