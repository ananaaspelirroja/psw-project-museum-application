import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exhibition } from '../../models/exhibition';

@Injectable({
  providedIn: 'root'
})
export class ExhibitionService {

  private apiUrl = 'http://localhost:9090/api/exhibitions'; // URL base per l'API backend

  constructor(private http: HttpClient) { }

  // Metodo per ottenere tutte le esibizioni
  getAllExhibitions(): Observable<Exhibition[]> {
    return this.http.get<Exhibition[]>(this.apiUrl);
  }
}
