// src/app/components/exhibition-list/exhibition-list.component.ts
import { Component, OnInit } from '@angular/core';
import {ExhibitionService} from "../../services/services/show-all-exhibitions/exhibition-service.service";
import {Exhibition} from "../../services/models/exhibition";


@Component({
  selector: 'app-exhibition-list',
  templateUrl: './exhibition-list.component.html',
  styleUrls: ['./exhibition-list.component.scss']
})
export class ExhibitionListComponent implements OnInit {

  exhibitions: Exhibition[] = []; // Array per memorizzare le esibizioni
  errorMessage: string = ''; // Messaggio di errore per eventuali problemi di caricamento

  constructor(private exhibitionService: ExhibitionService) { }

  ngOnInit(): void {
    this.loadExhibitions(); // Carica le esibizioni al caricamento del componente
  }

  // Metodo per caricare le esibizioni
  loadExhibitions(): void {
    this.exhibitionService.getAllExhibitions().subscribe({
      next: (data) => this.exhibitions = data,
      error: (err) => this.errorMessage = 'Error loading exhibitions: ' + err.message
    });
  }
}
