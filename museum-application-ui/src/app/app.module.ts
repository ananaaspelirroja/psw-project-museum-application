import {APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {AppRoutingModule} from "./app-routing.module";
import {ExhibitionListComponent} from "./pages/exhibition-list/exhibition-list.component";
import {ExhibitionService} from "./services/services/show-all-exhibitions/exhibition-service.service";
import {HttpClientModule} from "@angular/common/http";
import {KeycloakService} from "./services/services/keycloak/keycloak.service"; // Importa RouterModule qui


export function initializeKeycloak(keycloak: KeycloakService) {
  return () => keycloak.init().then(() => {
    console.log('Keycloak inizializzato');
  }).catch(error => {
    console.error('Errore durante l\'inizializzazione di Keycloak in APP_INITIALIZER', error);
  });
}


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ExhibitionListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule, // Aggiungi RouterModule qui per abilitare il routing
    AppRoutingModule // Importa AppRoutingModule per le configurazioni delle rotte
  ],
  providers: [
    ExhibitionService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      deps: [KeycloakService],
      multi: true,
    },
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Aggiungi questo schema
  bootstrap: [AppComponent]
})
export class AppModule { }
