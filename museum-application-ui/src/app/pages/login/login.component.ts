import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "../../services/services/keycloak/keycloak.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent  {
  constructor(
    private ss: KeycloakService
  ) {
  }


}
