import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { KeycloakService } from "../../services/services/keycloak/keycloak.service";
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
  exhibitionForm: FormGroup; // Form per una nuova esibizione
  showForm: boolean = false; // Controlla se il form è visibile

  constructor(
    private exhibitionService: ExhibitionService,
    private route: ActivatedRoute,
    private fb: FormBuilder,

    public keycloakService: KeycloakService // Verifica del ruolo utente
  ) {
    // Inizializza il form per una nuova esibizione
    this.exhibitionForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

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

  // Alterna la visibilità del form
  toggleForm(): void {
    this.showForm = !this.showForm;
  }

  // Metodo per aggiungere una nuova esibizione
  addExhibition(): void {
    if (this.exhibitionForm.valid) {
      const newExhibition: Exhibition = this.exhibitionForm.value;

      this.exhibitionService.addExhibition(newExhibition).subscribe({
        next: (createdExhibition) => {
          this.exhibitions.push(createdExhibition); // Aggiungi la nuova esibizione alla lista
          this.exhibitionForm.reset(); // Resetta il form
          this.showForm = false; // Nasconde il form
          alert('Esibizione aggiunta con successo!');
        },
        error: (err) => this.errorMessage = 'Error adding exhibition: ' + err.message
      });
    }
  }

  // Metodo per eliminare un'esibizione
  deleteExhibition(id: number | undefined): void {
    if (id === undefined) {
      console.error('ID dell\'esibizione non definito');
      return;
    }

    if (confirm('Sei sicuro di voler eliminare questa esibizione?')) {
      this.exhibitionService.deleteExhibition(id).subscribe({
        next: () => {
          this.exhibitions = this.exhibitions.filter(exhibition => exhibition.id !== id);
          alert('Esibizione eliminata con successo!');
        },
        error: (err) => console.error('Errore durante l\'eliminazione dell\'esibizione:', err)
      });
    }
  }

}
