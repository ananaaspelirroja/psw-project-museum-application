import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {AppRoutingModule} from "./app-routing.module";
import {ExhibitionListComponent} from "./pages/exhibition-list/exhibition-list.component"; // Importa RouterModule qui

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ExhibitionListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule, // Aggiungi RouterModule qui per abilitare il routing
    AppRoutingModule // Importa AppRoutingModule per le configurazioni delle rotte
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Aggiungi questo schema
  bootstrap: [AppComponent]
})
export class AppModule { }
