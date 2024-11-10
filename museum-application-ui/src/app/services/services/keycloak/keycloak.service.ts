import { Injectable } from '@angular/core';
import {UserProfile} from "./user-profile";
import Keycloak from 'keycloak-js';


@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;

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
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false
    });

    if (authenticated) {
      this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak.token || '';
    }
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
    // Ensure token is fresh
    if (this.keycloak.token) {
      await this.keycloak.updateToken(30).catch(() => {
        this.login(); // Redirect to login if token cannot be refreshed
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
}
