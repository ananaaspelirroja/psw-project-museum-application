import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {AppRoutingModule} from "./app-routing.module";
import {ExhibitionListComponent} from "./pages/exhibition-list/exhibition-list.component";
import {ExhibitionService} from "./services/services/show-all-exhibitions/exhibition-service.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {KeycloakService} from "./services/services/keycloak/keycloak.service";
import {MenuComponent} from "./modules/menu/menu.component";
import {TicketListComponent} from "./pages/ticket-list/ticket-list.component";
import {CartComponent} from "./modules/cart/cart.component";
import { CommonModule } from '@angular/common';
import {OrdersComponent} from "./pages/orders/orders.component";
import {HttpTokenInterceptor} from "./services/services/interceptors/http-token.interceptor.service";



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ExhibitionListComponent,
    MenuComponent,
    TicketListComponent,
    CartComponent,
    OrdersComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule, // Aggiungi RouterModule qui per abilitare il routing
    AppRoutingModule // Importa AppRoutingModule per le configurazioni delle rotte
  ],
  exports: [
  MenuComponent // opzionale, per permettere il suo utilizzo in altri moduli
  ],
  providers: [
    ExhibitionService,
    KeycloakService, // Mantieni il servizio disponibile senza `APP_INITIALIZER`
    { provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true }
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Aggiungi questo schema
  bootstrap: [AppComponent]
})
export class AppModule { }
