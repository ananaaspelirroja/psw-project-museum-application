import { Injectable } from '@angular/core';
import { UserProfile } from "./user-profile";
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private initialized = false; // Variabile per tracciare l'inizializzazione

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:8080',
        realm: 'museum-application',
        clientId: 'museum'
      });
    }
    return this._keycloak;
  }

  private _profile: UserProfile | undefined;

  get profile(): UserProfile | undefined {
    return this._profile;
  }

  async init(): Promise<void> {
    console.log('Inizio di Keycloak init');
    try {
      const authenticated = await this.keycloak.init({
        onLoad: 'check-sso', // Controlla solo lo stato della sessione, senza forzare il login
        checkLoginIframe: false,
      });

      if (authenticated) {
        console.log('Utente autenticato con successo');
        this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
        this._profile.token = this.keycloak.token || '';
      } else {
        console.warn('Utente non autenticato, avvio login');
        await this.login(); // Chiama esplicitamente login se l’utente non è autenticato
      }
    } catch (error) {
      console.error('Errore durante l\'inizializzazione di Keycloak', error);
    }
    console.log('Fine di Keycloak init');
  }



  login() {
    return this.keycloak.login();
  }

  logout() {
    return this.keycloak.logout({ redirectUri: 'http://localhost:4200' });
  }

  isAuthenticated(): boolean {
    return this.keycloak.authenticated || false;
  }

  async getToken(): Promise<string> {
    if (this.keycloak.token) {
      await this.keycloak.updateToken(30).catch(() => {
        this.login(); // Redirect se il token non può essere aggiornato
      });
      return this.keycloak.token!;
    }
    return '';
  }

  async getUserProfile(): Promise<UserProfile | undefined> {
    if (!this._profile) {
      this._profile = await this.keycloak.loadUserProfile();
    }
    return this._profile;
  }

  hasRole(role: string): boolean {
    return this.keycloak.hasRealmRole(role);
  }

  // Getter per il nome dell'utente
  get userName(): string {
    return this._profile?.firstName || 'Ospite';
  }
}
