import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from "../../services/services/keycloak/keycloak.service";
import { ExhibitionService } from "../../services/services/show-all-exhibitions/exhibition-service.service";
import { Exhibition } from "../../services/models/exhibition";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {
  searchQuery: string = ''; // Query di ricerca
  exhibitions: Exhibition[] = []; // Risultati della ricerca

  constructor(
    public keycloakService: KeycloakService,
    private exhibitionService: ExhibitionService,
    private router: Router
  ) {}

  // Metodo di logout
  logout(): void {
    this.keycloakService.logout();
  }

  // Metodo per gestire la ricerca delle esibizioni
  onSearch(): void {
    if (this.searchQuery.trim()) {
      // Naviga alla pagina delle esibizioni con la query di ricerca come parametro
      this.router.navigate(['/exhibitions'], { queryParams: { search: this.searchQuery } });
    } else {
      // Se la query è vuota, mostra tutte le esibizioni
      this.showAllExhibitions();
    }
  }

  // Metodo per mostrare tutte le esibizioni
  showAllExhibitions(): void {
    this.router.navigate(['/exhibitions']);
  }
}

