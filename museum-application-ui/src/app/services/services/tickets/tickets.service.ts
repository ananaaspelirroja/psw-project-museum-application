
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Ticket} from "../../models/ticket";

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl = 'http://localhost:9090/api/tickets'; // Sostituisci con l'URL corretto

  constructor(private http: HttpClient) {}

  getAllTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.apiUrl}`);
  }

  createTicket(ticket: Partial<Ticket>): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.apiUrl}`, ticket);
  }

  updateTicketQuantity(ticketId: number, newQuantity: number): Observable<Ticket> {
    const params = new HttpParams().set('quantity', newQuantity.toString());
    return this.http.put<Ticket>(`${this.apiUrl}/${ticketId}/quantity`, null, { params });
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
