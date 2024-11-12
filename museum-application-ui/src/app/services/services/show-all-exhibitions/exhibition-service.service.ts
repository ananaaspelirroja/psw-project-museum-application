import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

  // Metodo per cercare esibizioni per nome
  searchExhibitions(name: string): Observable<Exhibition[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Exhibition[]>(`${this.apiUrl}/search`, { params });
  }

  // Metodo per aggiungere un'esibizione
  addExhibition(exhibition: Exhibition): Observable<Exhibition> {
    return this.http.post<Exhibition>(`${this.apiUrl}`, exhibition);
  }

  deleteExhibition(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getExhibitionById(id: number): Observable<Exhibition> {
    return this.http.get<Exhibition>(`${this.apiUrl}/${id}`);
  }
}
