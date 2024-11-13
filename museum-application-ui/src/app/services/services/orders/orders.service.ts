import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  private apiUrl = 'http://localhost:9090/api/my-tickets';

  constructor(private http: HttpClient) {}

  // Metodo per creare un ordine
  createOrder(orderData: { ticketId: number; quantity: number }[], totalAmount: number): Observable<any> {
    return this.http.post(this.apiUrl, { items: orderData, totalAmount });
  }
}
