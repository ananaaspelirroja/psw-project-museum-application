import { Component, OnInit } from '@angular/core';
import { KeycloakService } from "../../services/services/keycloak/keycloak.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  constructor(
    private ss: KeycloakService,
    private router: Router
  ) {}

  async ngOnInit() {
    await this.ss.init();
    if (this.ss.isAuthenticated()) {
      // Naviga alla home o a un’altra pagina dopo il login
      this.router.navigate(['/']);
    }
  }
}
