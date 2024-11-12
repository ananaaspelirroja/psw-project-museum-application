
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  updateTicketQuantity(id: number, quantity: number): Observable<Ticket> {
    return this.http.patch<Ticket>(`${this.apiUrl}/${id}`, { quantity });
  }

  deleteTicket(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
