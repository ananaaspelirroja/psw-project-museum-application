import { Component, OnInit } from '@angular/core';
import { ExhibitionService } from "../../services/services/show-all-exhibitions/exhibition-service.service";
import { Exhibition } from "../../services/models/exhibition";
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-exhibition-list',
  templateUrl: './exhibition-list.component.html',
  styleUrls: ['./exhibition-list.component.scss']
})
export class ExhibitionListComponent implements OnInit {
  exhibitions: Exhibition[] = []; // Array per memorizzare le esibizioni
  errorMessage: string = ''; // Messaggio di errore per eventuali problemi di caricamento
  searchQuery: string | null = null; // Query di ricerca

  constructor(private exhibitionService: ExhibitionService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    // Ottiene la query di ricerca dai parametri della rotta, se presente
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'] || null;
      if (this.searchQuery) {
        this.searchExhibitions(this.searchQuery); // Se c'è una query di ricerca, esegue la ricerca
      } else {
        this.loadExhibitions(); // Altrimenti, carica tutte le esibizioni
      }
    });
  }

  // Metodo per caricare tutte le esibizioni
  loadExhibitions(): void {
    this.exhibitionService.getAllExhibitions().subscribe({
      next: (data) => this.exhibitions = data,
      error: (err) => this.errorMessage = 'Error loading exhibitions: ' + err.message
    });
  }

  // Metodo per cercare le esibizioni per nome
  searchExhibitions(name: string): void {
    this.exhibitionService.searchExhibitions(name).subscribe({
      next: (data) => this.exhibitions = data,
      error: (err) => this.errorMessage = 'Error searching exhibitions: ' + err.message
    });
  }
}
