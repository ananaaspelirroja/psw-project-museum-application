import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import {KeycloakService} from "../keycloak/keycloak.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private keycloakService: KeycloakService, private router: Router) {}

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    await this.keycloakService.init(); // Assicura l’inizializzazione di Keycloak

    const expectedRole = route.data['role'];
    const hasRole = this.keycloakService.hasRole(expectedRole);

    if (!hasRole) {
      this.router.navigate(['/unauthorized']); // Reindirizza se l’utente non ha il ruolo richiesto
      return false;
    }
    return true;
  }
}
