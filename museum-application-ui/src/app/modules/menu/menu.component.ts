import { Component } from '@angular/core';
import {KeycloakService} from "../../services/services/keycloak/keycloak.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {
  constructor(public keycloakService: KeycloakService) {}

  logout(): void {
    this.keycloakService.logout();
  }

}
